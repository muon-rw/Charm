package svenhjol.charm.feature.mooblooms;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SuspiciousStewItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import svenhjol.charm.Charm;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"deprecation", "unused"})
public class MoobloomEntity extends Cow implements Shearable {
    private static final String TYPE_TAG = "Type";
    private static final String POLLINATED_TAG = "Pollinated";
    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> POLLINATED = SynchedEntityData.defineId(MoobloomEntity.class, EntityDataSerializers.BOOLEAN);
    public static Map<Type, ResourceLocation> TEXTURES = new HashMap<>();

    public MoobloomEntity(EntityType<? extends Cow> entityType, Level level) {
        super(entityType, level);

        // Set up the textures for each moobloom type.
        TEXTURES = Util.make(Maps.newHashMap(), map -> {
            for (var type : Type.values()) {
                map.put(type, new ResourceLocation(Charm.MOD_ID, "textures/entity/moobloom/" + type.name + ".png"));
            }
        });
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData entityData, @Nullable CompoundTag entityTag) {
        entityData = super.finalizeSpawn(level, difficulty, spawnReason, entityData, entityTag);

        var types = Arrays.asList(Type.values());
        var type = types.get(random.nextInt(types.size()));
        setMoobloomType(type);

        return entityData;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(TYPE, Type.ALLIUM.name());
        entityData.define(POLLINATED, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new MoobloomPlantFlowerGoal(this));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        var held = player.getItemInHand(hand);

        if (held.getItem() == Items.BOWL && !isBaby()) {
            if (!level.isClientSide() && isPollinated()) {
                ItemStack stew;
                var optional = getEffectFromFlower(getMoobloomType().flower);

                if (optional.isPresent()) {
                    var effectFromFlower = optional.get();
                    var effect = effectFromFlower.getLeft();
                    var duration = effectFromFlower.getRight() * 2;

                    stew = new ItemStack(Items.SUSPICIOUS_STEW);
                    playSound(SoundEvents.MOOSHROOM_MILK_SUSPICIOUSLY, 1.0F, 1.0F);
                    SuspiciousStewItem.saveMobEffect(stew, effect, duration);
                } else {
                    stew = new ItemStack(Items.MUSHROOM_STEW);
                    playSound(SoundEvents.MOOSHROOM_MILK, 1.0F, 1.0F);
                }

                var out = ItemUtils.createFilledResult(held, player, stew, false);
                player.setItemInHand(hand, out);
                entityData.set(POLLINATED, false);

                // TODO: Advancement
            }

            return InteractionResult.sidedSuccess(level.isClientSide());

        } else if (held.getItem() == Items.SHEARS && readyForShearing()) {

            shear(SoundSource.PLAYERS);
            if (!level.isClientSide()) {
                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString(TYPE_TAG, getMoobloomType().name);
        tag.putBoolean(POLLINATED_TAG, entityData.get(POLLINATED));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setMoobloomType(Type.fromName(tag.getString(TYPE_TAG)));

        if (tag.contains(POLLINATED_TAG)) {
            entityData.set(POLLINATED, tag.getBoolean(POLLINATED_TAG));
        }
    }

    @Override
    public MoobloomEntity getBreedOffspring(ServerLevel level, AgeableMob mob) {
        var entity = Mooblooms.ENTITY.get().create(level);
        var childType = level.random.nextFloat() < 0.5F ?
            getMoobloomType() : ((MoobloomEntity)mob).getMoobloomType();

        if (entity != null) {
            entity.setMoobloomType(childType);
            return entity;
        }

        return null;
    }

    public void pollinate() {
        level.playSound(null, blockPosition(), SoundEvents.BEE_POLLINATE, SoundSource.NEUTRAL, 1.0F, 1.0F);
        entityData.set(POLLINATED, true);
    }

    public boolean isPollinated() {
        return entityData.get(POLLINATED);
    }

    public Type getMoobloomType() {
        return Type.fromName(entityData.get(TYPE));
    }

    public void setMoobloomType(Type type) {
        entityData.set(TYPE, type.name);
    }

    public ResourceLocation getMoobloomTexture() {
        return TEXTURES.getOrDefault(getMoobloomType(), TEXTURES.get(Type.ALLIUM));
    }

    public Optional<Pair<MobEffect, Integer>> getEffectFromFlower(BlockState flower) {
        var block = flower.getBlock();
        if (block instanceof FlowerBlock flowerBlock) {
            return Optional.of(Pair.of(flowerBlock.getSuspiciousEffect(), flowerBlock.getEffectDuration()));
        }

        return Optional.empty();
    }

    public static boolean canSpawn(EntityType<MoobloomEntity> type, LevelAccessor level, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        return level.getRawBrightness(pos, 0) > 8;
    }

    /**
     * Copypasta from Mooshroom entity.
     */
    @Override
    public void shear(SoundSource shearedSoundCategory) {
        level.playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, shearedSoundCategory, 1.0F, 1.0F);

        if (!level.isClientSide()) {
            ((ServerLevel)level).sendParticles(ParticleTypes.EXPLOSION, getX(), getY(0.5D), getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
            discard();

            var cow = EntityType.COW.create(level);
            if (cow == null) return;

            cow.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
            cow.setHealth(getHealth());
            cow.yBodyRot = yBodyRot;

            if (hasCustomName()) {
                cow.setCustomName(getCustomName());
                cow.setCustomNameVisible(isCustomNameVisible());
            }

            if (isPersistenceRequired()) {
                cow.setPersistenceRequired();
            }

            cow.setInvulnerable(isInvulnerable());
            level.addFreshEntity(cow);

            var flower = new ItemStack(getMoobloomType().flower.getBlock());
            for (int i = 0; i < 5; ++i) {
                level.addFreshEntity(new ItemEntity(level, getX(), getY(1.0D), getZ(), flower));
            }
        }
    }

    @Override
    public boolean readyForShearing() {
        return isAlive() && !isBaby();
    }

    public enum Type {
        ALLIUM("allium", Blocks.ALLIUM.defaultBlockState()),
        AZURE_BLUET("azure_bluet", Blocks.AZURE_BLUET.defaultBlockState()),
        BLUE_ORCHID("blue_orchid", Blocks.BLUE_ORCHID.defaultBlockState()),
        CORNFLOWER("cornflower", Blocks.CORNFLOWER.defaultBlockState()),
        DANDELION("dandelion", Blocks.DANDELION.defaultBlockState()),
        LILY_OF_THE_VALLEY("lily_of_the_valley", Blocks.LILY_OF_THE_VALLEY.defaultBlockState()),
        ORANGE_TULIP("orange_tulip", Blocks.ORANGE_TULIP.defaultBlockState()),
        PINK_TULIP("pink_tulip", Blocks.PINK_TULIP.defaultBlockState()),
        RED_TULIP("red_tulip", Blocks.RED_TULIP.defaultBlockState()),
        WHITE_TULIP("white_tulip", Blocks.WHITE_TULIP.defaultBlockState()),
        OXEYE_DAISY("oxeye_daisy", Blocks.OXEYE_DAISY.defaultBlockState()),
        POPPY("poppy", Blocks.POPPY.defaultBlockState());

        private final String name;
        private final BlockState flower;

        Type(String name, BlockState flower) {
            this.name = name;
            this.flower = flower;
        }

        public BlockState getFlower() {
            return this.flower;
        }

        private static Type fromName(String name) {
            for (var value : values()) {
                if (value.name.equals(name)) {
                    return value;
                }
            }

            return ALLIUM;
        }
    }
}
