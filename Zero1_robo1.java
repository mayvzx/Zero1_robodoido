import robocode.*;
import java.awt.Color;
import robocode.util.Utils;

public class Zero1_robo1 extends AdvancedRobot {

    public void run() {
        setColors(Color.blue, Color.black, Color.white);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        while (true) {
            setTurnRadarRight(60);  // Radar busca inimigo
            // Movimento (suavização de borda)
            if (getX() < 60 || getY() < 60 
                || getBattleFieldWidth() - getX() < 60 
                || getBattleFieldHeight() - getY() < 60) {
                setTurnRight(90);
                setAhead(100);
            } else {
                setAhead(150);
                setTurnRight(Math.random() * 60);
            }
            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        double radarTurn = getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians();
        setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));

        double firePower = Math.min(400 / e.getDistance(), 3);
        double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
        double predictedX = getX() + Math.sin(absoluteBearing) * e.getDistance();
        double predictedY = getY() + Math.cos(absoluteBearing) * e.getDistance();
        double gunTurn = Utils.normalRelativeAngle(
            Math.atan2(predictedX - getX(), predictedY - getY()) - getGunHeadingRadians()
        );
        setTurnGunRightRadians(gunTurn);
        setFire(firePower);
    }

    public void onHitByBullet(HitByBulletEvent e) {
        setBack(50);
        setTurnRight(90 - e.getBearing());
    }

    public void onHitWall(HitWallEvent e) {
        setBack(100);
        setTurnRight(90);
        execute();
    }
}
