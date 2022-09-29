package svenhjol.charm.module.mooblooms;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

public class BeeMoveToMoobloomGoal extends Goal {
    private static final int MAX_MOVE_TICKS = 1200;
    private static final int RANGE = 24;

    private final Bee bee;
    private final Level level;
    private MoobloomEntity moobloom = null;
    private int moveTicks;
    private int lastTried = 0;

    public BeeMoveToMoobloomGoal(Bee bee) {
        this.bee = bee;
        this.level = bee.level;
    }

    @Override
    public boolean canUse() {
        if (bee.hasNectar()) {
            return --lastTried <= 0;
        }

        return false;
    }

    @Override
    public void start() {
        moobloom = null;
        moveTicks = 0;
        bee.resetTicksWithoutNectarSinceExitingHive();

        AABB box = bee.getBoundingBox().inflate(RANGE, RANGE / 2.0, RANGE);
        Predicate<MoobloomEntity> selector = entity -> !entity.isPollinated() && entity.isAlive();
        List<MoobloomEntity> entities = level.getEntitiesOfClass(MoobloomEntity.class, box, selector);

        if (entities.size() > 0) {
            moobloom = entities.get(level.random.nextInt(entities.size()));
            bee.setStayOutOfHiveCountdown(MAX_MOVE_TICKS);
        } else {
            lastTried = 200;
        }

        super.start();
    }

    @Override
    public void stop() {
        moveTicks = 0;
        moobloom = null;
        bee.getNavigation().stop();
        bee.getNavigation().resetMaxVisitedNodesMultiplier();
    }

    @Override
    public boolean canContinueToUse() {
        return moobloom != null && moobloom.isAlive() && moveTicks < MAX_MOVE_TICKS;
    }

    @Override
    public void tick() {
        moveTicks++;
        if (moobloom == null || !moobloom.isAlive()) return;

        if (moveTicks > MAX_MOVE_TICKS) {
            moobloom = null;
        } else if (!bee.getNavigation().isInProgress()) {
            bee.pathfindRandomlyTowards(moobloom.blockPosition());
        } else {

            // update bee tracking to take into account a moving moobloom
            if (moveTicks % 50 == 0) {
                bee.pathfindRandomlyTowards(moobloom.blockPosition());
            }

            double dist = bee.position().distanceTo(moobloom.position());
            if (dist < 2.2) {
                bee.setHasNectar(false);
                moobloom.pollinate();
                moobloom = null;
            }
        }
    }
}
