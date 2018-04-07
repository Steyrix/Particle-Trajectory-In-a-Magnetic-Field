package myutil;

import myutil.LinearMath;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

public class TrajectoryDrawer {
    private TrajectoryDrawer() {
    }

    public static Chart getInitialChart() {
        Mapper mapper = new Mapper() {
            public double f(double x, double y) {
                return Math.sin(x) + Math.cos(y);
            }
        };

        Range range = new Range(-150, 150);
        int steps = 50;
        Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);
        surface.setWireframeColor(Color.BLACK);
        Chart chart = AWTChartComponentFactory.chart(Quality.Advanced, IChartComponentFactory.Toolkit.swing);
        chart.getScene().getGraph().add(surface);
        return chart;
    }

    public static Chart getChart(double[] pVelocity, double[] bVector, double[] eVector, double qCharge, double pMass, double timeStep) {

        Chart chart = AWTChartComponentFactory.chart(Quality.Advanced, IChartComponentFactory.Toolkit.swing);
        LineStrip trajectoryStrip = getLineStrip(pVelocity, bVector, eVector, qCharge, pMass, timeStep);
        trajectoryStrip.setDisplayed(true);
        trajectoryStrip.setFaceDisplayed(true);
        trajectoryStrip.setWireframeDisplayed(false);
        chart.getScene().getGraph().add(trajectoryStrip);
        chart.getCanvas().addMouseController(new AWTCameraMouseController());
        return chart;
    }

    private static LineStrip getLineStrip(double[] pVelocity, double[] bVector, double[] eVector, double qCharge, double pMass, double timeStep) {

        LineStrip outLineStrip = new LineStrip();

        double[] force = new double[3];
        double[] acceleration = new double[3];
        double[] position = new double[3];
        double[] initVel = pVelocity;
        boolean nanFlag = false;
        for (float step = 0.0f; step <= timeStep; step += 0.00001f) {

            double[] velBcross = LinearMath.getCrossProduct(pVelocity, bVector);
            for (int i = 0; i < 3; i++) {
                force[i] = (qCharge / Math.pow(10, 9)) * velBcross[i] + Math.abs(qCharge / Math.pow(10, 9)) * eVector[i];
                acceleration[i] = force[i] / pMass;
                pVelocity[i] = initVel[i] + acceleration[i] * step;
                position[i] += pVelocity[i] * step;
                if(Double.isNaN(force[i]) || Double.isNaN(acceleration[i]) || Double.isNaN(pVelocity[i]) || Double.isNaN(position[i]))
                {
                    nanFlag = true;
                    break;
                }
            }

            if(nanFlag)
                break;

            Coord3d cords = new Coord3d(position[0], position[1], position[2]);
            Point point = new Point(cords, new Color(1, 0, 0, 0.5f));
            outLineStrip.add(point);
        }

        return outLineStrip;
    }

}
