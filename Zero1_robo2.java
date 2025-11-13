import robocode.*;
import java.awt.*;
import robocode.util.Utils;

public class Zero1_robo2 extends AdvancedRobot {
    boolean movingForward;

    public void run() {
        setBodyColor(new Color(150, 0, 180));
        setGunColor(new Color(80, 50, 230));
        setRadarColor(new Color(0, 100, 150));
        setBulletColor(new Color(255, 200, 50));
        setScanColor(new Color(255, 255, 200));

        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        setTurnRadarRight(Double.POSITIVE_INFINITY);

        while (true) {
            setAhead(40000);
            movingForward = true;

            setTurnRight(90);
            waitFor(new TurnCompleteCondition(this));

            setTurnLeft(180);
            waitFor(new TurnCompleteCondition(this));

            setTurnRight(180);
            waitFor(new TurnCompleteCondition(this));
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        double radarTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
        setTurnRadarRightRadians(2 * Utils.normalRelativeAngle(radarTurn));

        double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        double gunTurn = Utils.normalRelativeAngle(absoluteBearing - getGunHeadingRadians());
        setTurnGunRightRadians(gunTurn);

        if (Math.abs(getGunTurnRemaining()) < Math.PI / 18) {
            setFire(1.5);
        }
    }

    public void onHitWall(HitWallEvent e) {
        reverseDirection();
    }

    public void reverseDirection() {
        if (movingForward) {
            setBack(40000);
            movingForward = false;
        } else {
            setAhead(40000);
            movingForward = true;
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        if (e.isMyFault()) {
            reverseDirection();
        }
    }
}
