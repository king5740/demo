package com.example.demo;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class drawHome {
    public static void drawHome(Stage stage) {


        Group group = new Group();

        drawFrame(group);
        drawLines(group);
        drawSensors(group);
        drawLinesBetweenHeadAndSensor(group);
        drawHeadAndForwarder(group);

        Scene Demo_Scene = new Scene(group, 800, 800, Color.BEIGE);

        stage.setTitle("Draw Rectangle");
        stage.setScene(Demo_Scene);
    }


    /**
     * katta to'rtburchkni chizish
     */
    private static void drawFrame(Group group) {
        Rectangle rectangle = new Rectangle();

        rectangle.setX(Tool.getLocationX(0));
        rectangle.setY(Tool.getLocationY(150));
        rectangle.setWidth(600.0f);
        rectangle.setHeight(600.0f);

        rectangle.setFill(Color.WHITE);
        rectangle.setStrokeWidth(1.0);
        rectangle.setStroke(Color.BLACK);

        group.getChildren().add(rectangle);
    }


    /**
     * ustun va qatorlarni chizish
     */
    private static void drawLines(Group group) {
        //ustunlar

        Line ustun1 = getLine(37.5, 150, 37.5, 0);
        group.getChildren().add(ustun1);

        Line ustun2 = getLine(112.5, 150, 112.5, 0);
        group.getChildren().add(ustun2);

        Line ustun3 = getLine(75, 125, 75, 100);
        group.getChildren().add(ustun3);

        Line ustun4 = getLine(75, 50, 75, 0);
        group.getChildren().add(ustun4);


        Line qator1 = getLine(37.5, 125, 112.5, 125);
        group.getChildren().add(qator1);

        Line qator2 = getLine(37.5, 100, 112.5, 100);
        group.getChildren().add(qator2);

        Line qator3 = getLine(37.5, 50, 112.5, 50);
        group.getChildren().add(qator3);


    }


    /**
     * chiziqni boshlanish va tugash nuqtalari berilsa line qilib x va y larini to'g'tilab qaytaradi
     */
    private static Line getLine(double startX, double startY, double endX, double endY) {
        Line line = new Line();

        line.setStartX(Tool.getLocationX(startX));
        line.setStartY(Tool.getLocationY(startY));

        line.setEndX(Tool.getLocationX(endX));
        line.setEndY(Tool.getLocationY(endY));

        return line;
    }

    /**
     * sensorning sharchalarini chizadi
     */
    private static void drawSensors(Group group) {

        for (int i = 0; i < SensorService.sensorHashMap.size(); i++) {

            Circle tempCircle = drawCircle(SensorService.sensorHashMap.get(i));

            group.getChildren().add(tempCircle);
        }
    }

    private static void drawHeadAndForwarder(Group group) {
        Circle circleForwarder = drawCircle(SensorService.sensorForwarder);
        Circle circleHead = drawCircle(SensorService.sensorBase);

        group.getChildren().add(circleForwarder);
           group.getChildren().add(circleHead);


    }

    private static Circle drawCircle(Sensor sensor) {
        Circle circle = new Circle();
        try {
            circle.setCenterX(Tool.getLocationX(sensor.getX()));
            circle.setCenterY(Tool.getLocationY(sensor.getY()));
            circle.setRadius(5);
        } catch (NullPointerException e) {
            System.out.println();
        }
        return circle;
    }

    private static void drawLinesBetweenHeadAndSensor(Group group) {

        for (int i = 0; i < 100; i++) {

            Sensor sensor = SensorService.sensorHashMap.get(i);
            Sensor headSensor = SensorService.sensorHashMap.get(sensor.getHeadId());

            Line line = drawLinesBetweenTwoSensor(sensor, headSensor);

            group.getChildren().add(line);
        }
    }

    private static Line drawLinesBetweenTwoSensor(Sensor sensor1, Sensor sensor2) {
        return getLine(sensor1.getX(), sensor1.getY(), sensor2.getX(), sensor2.getY());
    }

}
