/*import java.util.*;
import java.io.*;
import java.math.*;
import java.util.Random.*;
import java.util.ArrayList.*;

*//**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **//*
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        Controller controller = new Controller();

        // game loop
        while (true) {
            int myShipCount = in.nextInt(); // the number of remaining ships
            int entityCount = in.nextInt(); // the number of entities (e.g. ships, mines or cannonballs)
            for (int i = 0; i < entityCount; i++) {
                int entityId = in.nextInt();
                String entityType = in.next();
                int x = in.nextInt();
                int y = in.nextInt();
                int arg1 = in.nextInt();
                int arg2 = in.nextInt();
                int arg3 = in.nextInt();
                int arg4 = in.nextInt();

                *//*System.err.println(entityType);
                System.err.println(x);
                System.err.println(y);
                System.err.println(arg1);
                System.err.println(arg2);
                System.err.println(arg3);
                System.err.println(arg4);

                Entity entity = new Entity();
                entity.setEntityId(entityId);
                entity.setEntityType(entityType);
                entity.setxBarrel(x);
                entity.setyBarrel(y);
                entity.setRotation(arg1);
                entity.setSpeed(arg2);
                entity.setLife(arg3);
                entity.setControl(arg4);*//*


                Entity entity = new Entity();
                entity.setEntityId(in.nextInt());
                entity.setEntityType(in.next());
                entity.setxBarrel(in.nextInt());
                entity.setyBarrel(in.nextInt());
                entity.setRotation(in.nextInt());
                entity.setSpeed(in.nextInt());
                entity.setLife(in.nextInt());
                entity.setControl(in.nextInt());

                controller.addEntity(entity);
            }
            for (int i = 0; i < myShipCount; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");
                System.err.println(" werwerewr");
                Random r = new Random();
                int a = r.nextInt(23);
                int b = r.nextInt(21);


                controller.getMyShip();

                controller.getClosser();
                System.out.println(Action.MOVE + " " + controller.getxBarrel() + " " + controller.getyBarrel());
                // Any valid action, such as "WAIT" or "MOVE x y"
            }
        }
    }
}

enum EntityTypes{
    BARREL, SHIP, CANNONBALL;
}

enum Action{
    MOVE, SLOWER, WAIT, MINE, FIRE;

}


class Controller{
    private List<Entity> entityes = new ArrayList<>();
    private int x = 1000;
    private int y = 1000;
    private int myX;
    private int myY;

    public void addEntity(Entity entity){
        entityes.add(entity);
    }

    public void getClosser(){
        for (Entity ent : entityes){
            if (ent.getEntityType().equals("BARREL")){
                if ((Math.abs(myX - ent.getxBarrel()) + Math.abs(myY - ent.getyBarrel())) < (Math.abs(myX - x) + Math.abs(myY - y))){
                    x = ent.getxBarrel();
                    y = ent.getyBarrel();
                }
            }
        }
    }


    public void getMyShip(){
        System.err.println(" werwerewr");

        for (Entity ent : entityes){

            *//*System.err.println(ent.getEntityType());
            System.err.println(ent.getxBarrel());
            System.err.println(ent.getyBarrel());
            System.err.println(ent.getRotation());
            System.err.println(ent.getSpeed());
            System.err.println(ent.getLife());
            System.err.println(ent.getControl());*//*

            System.err.println("--------");
            System.err.println(" werwerewr");
            if (ent.getEntityType().equals("SHIP") && ent.getControl() == 1){
                myX = ent.getxBarrel();
                myY = ent.getyBarrel();
                System.err.println(myX + " " + myY);
            }
        }
    }

    public int getxBarrel(){
        return x;
    }

    public int getyBarrel(){
        return x;
    }
}

class Entity{
    private int myShipCount;
    private int entityId;
    private String entityType;
    private int x;
    private int y;
    private int arg1;
    private int arg2;
    private int arg3;
    private int arg4;

    public int getMyShipCount(){
        return myShipCount;
    }

    public void setMyShipCount(int myShipCount){
        this.myShipCount = myShipCount;
    }

    public int getEntityId(){
        return entityId;
    }

    public void setEntityId (int entityId){
        this.entityId = entityId;
    }

    public String getEntityType(){
        return entityType;
    }

    public void setEntityType(String entityType){
        this.entityType = entityType;
    }

    public int getxBarrel(){
        return x;
    }

    public void setxBarrel(int x){
        this.x = x;
    }

    public int getyBarrel(){
        return y;
    }

    public void setyBarrel(int y){
        this.y = y;
    }

    public int getRotation(){
        return arg1;
    }

    public void setRotation(int arg){
        this.arg1 = arg;
    }

    public int getSpeed(){
        return arg2;
    }

    public void setSpeed(int arg){
        this.arg2 = arg;
    }

    public int getLife(){
        return arg3;
    }

    public void setLife(int arg){
        this.arg3 = arg;
    }

    public int getControl(){
        return arg4;
    }

    public void setControl(int arg){
        this.arg4 = arg;
    }
}*/
























