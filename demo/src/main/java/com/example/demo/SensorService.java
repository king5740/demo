package com.example.demo;

import java.util.HashMap;

public class SensorService {
    static int numberOfDiedSensors = 0;
    public static HashMap<Integer, Sensor> sensorHashMap = new HashMap<>();

    public static Sensor sensorForwarder;
    public static Sensor sensorBase;

    public static void createSensors() {
        int id = 0;
        for (int i = 1; i < 5; i++) {
            for (int j = 1; j < 7; j++) {

                if ((i == 2 || i == 3) && (j == 3 || j == 4))
                    continue;

                for (int k = 0; k < 5; k++) {

                    //  ((Math.random() * (max - min)) + min);
                    double max = i * 37.5 - 1;
                    double min = (i - 1) * 37.5 + 1;
                    double tempX = (Math.random() * (max - min)) + min;

                    int maxY = j * 25 - 1;
                    int minY = (j - 1) * 25 + 2;
                    double tempY = (Math.random() * (maxY - minY)) + minY;

                    Sensor tempSensor = new Sensor(tempX, tempY, id++);

                    sensorHashMap.put(tempSensor.getId(), tempSensor);
                }
            }
        }
        sensorBase = new Sensor(75, 170, -1);
        sensorForwarder = new Sensor(75, 65, -2);

        sensorHashMap.put(sensorBase.getId(), sensorBase);
        sensorHashMap.put(sensorForwarder.getId(), sensorForwarder);

        findHeads();
    }

    private static void findHeads() {
        int id = 0;

        for (int i = 1; i < 5; i++) {

            for (int j = 1; j < 7; j++) {

                if ((i == 2 || i == 3) && (j == 3 || j == 4))
                    continue;

                Sensor[] sensors = new Sensor[5];

                for (int k = 0; k < 5; k++) {
                    sensors[k] = sensorHashMap.get(id++);
                }
                findHeadFor5(sensors);
            }
        }
        findBS();
    }

    private static void findBS() {
        for (int i = 0; i < 100; i++) {

            try {


                if (0 > sensorHashMap.get(i).getHeadId()) {
                    Sensor sensor = sensorHashMap.get(i);
                    Double distanceForwarder = distanceBetweenTwoSensor(sensor, sensorForwarder);
                    Double distanceHead = distanceBetweenTwoSensor(sensor, sensorBase);

                    sensorHashMap.get(i).setHeadId(
                            (distanceHead < distanceForwarder) ? sensorBase.getId() : sensorForwarder.getId()
                    );
                }
            } catch (NullPointerException e) {
                System.out.println();
            }
        }
    }

    private static void findHeadFor5(Sensor[] sensors) {
        double minDistance = Integer.MAX_VALUE;
        int headId = -1;

        for (int i = 0; i < sensors.length; i++) {
            double tempDistance = 0;

            for (int j = 0; j < sensors.length; j++) {

                tempDistance += distanceBetweenTwoSensor(sensors[i], sensors[j]);
            }

            if (tempDistance < minDistance) {
                minDistance = tempDistance;
                headId = sensors[i].getId();
            }
        }
        changeHeadId(sensors, headId);
    }

    public static void changeHeadId(Sensor[] sensors, int headId) {
        sensorHashMap.get(headId).setHead(true);
        for (int i = 0; i < sensors.length; i++) {
            if (sensors[i].getId() != headId)
                sensorHashMap.get(sensors[i].getId()).setHeadId(headId);
        }
    }

    public static Double distanceBetweenTwoSensor(Sensor sensor1, Sensor sensor2) {

        return distanceBetweenTwoPoint(sensor1.getX(), sensor1.getY(), sensor2.getX(), sensor2.getY());

    }

    public static Double distanceBetweenTwoPoint(double x1, double y1, double x2, double y2) {
        return Math.sqrt(
                Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2)
        );
    }

    public void beginProcess() {
        int countRound = 0;
        while (countRound < 3000) {
            if (beginRound()) {
                System.out.println(numberOfDiedSensors);
            }
            System.out.println(countRound);
            countRound++;
        }
        System.out.println(numberOfDiedSensors);
    }

    private boolean beginRound() {
        boolean isClusterAlive = false;

        for (int i = 0; i < 100; i += 5) {
            Integer headId = -1;

            if (sensorHashMap.get(i).isHead())
                headId = i;
            else
                headId = sensorHashMap.get(i).getHeadId();

            if (beginInOneCluster(headId, i)) {
                isClusterAlive = true;
            }
        }
        return isClusterAlive;
    }

    private boolean beginInOneCluster(int headId, int id) {
        Sensor sensorHead = sensorHashMap.get(headId);

        double distanceToBase = Math.min(
                distanceBetweenTwoSensor(sensorHead, sensorBase),
                distanceBetweenTwoSensor(sensorHead, sensorForwarder));

        boolean sent = false;
        boolean hasDead = false;

        for (int i = id; i < id + 5; i++) {

            if (sensorHashMap.get(i).isHead())
                continue;
            else {
                if (sensorHashMap.get(i).isActive()) {
                    if (sensorHashMap.get(headId).isValidHead(distanceToBase)) {
                        if (sensorHashMap.get(i).isValidSensor(
                                distanceBetweenTwoSensor(sensorHead, sensorHashMap.get(i)))) {

                            sensorHashMap.get(i).sendToHead(distanceBetweenTwoSensor(sensorHead, sensorHashMap.get(i)));
                            sensorHashMap.get(headId).sendToStation(distanceToBase);
                            sent = true;

                            if (!sensorHashMap.get(i).isValidSensor(distanceBetweenTwoSensor(sensorHead, sensorHashMap.get(i)))) {
                                hasDead = true;
                                sensorHashMap.get(i).setActive(false);
                            }
                        } else {
                            hasDead = true;
                            numberOfDiedSensors++;
                            sensorHashMap.get(i).setActive(false);
                        }
                    } else {
                        reCluster(headId, id);
                    }
                }
            }
        }
        return sent;

    }

    private void reCluster(int headId, int id) {
        sensorHashMap.get(headId).setHead(false);

        double maxEnergy = Integer.MIN_VALUE;
        int newHeadId = -10;

        for (int i = id; i < id + 5; i++) {
            if (sensorHashMap.get(i).isActive() && maxEnergy < sensorHashMap.get(i).getEnergyTemp()) {
                maxEnergy = sensorHashMap.get(i).getEnergyTemp();
                newHeadId = i;
            }
        }
        if (newHeadId == -10) {
            return;
        } else {
            if (!sensorHashMap.get(newHeadId).isValidHead(Math.min(
                    distanceBetweenTwoSensor(sensorHashMap.get(newHeadId), sensorBase),
                    distanceBetweenTwoSensor(sensorHashMap.get(newHeadId), sensorForwarder)
            ))) {
                for (int i = id; i < id + 5; i++) {
                    if (sensorHashMap.get(i).isActive())
                        numberOfDiedSensors++;
                    sensorHashMap.get(i).setActive(false);
                }
            }
        }

    }


}
