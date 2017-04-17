/*
import java.util.*;

*
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 *

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
                int rotation = in.nextInt();
                int speed = in.nextInt();
                int life = in.nextInt();
                int control = in.nextInt();



System.err.println(entityId);
                System.err.println(entityType);
                System.err.println(x);
                System.err.println(y);
                System.err.println(rotation);
                System.err.println(speed);
                System.err.println(life);
                System.err.println(control);



Entity entity = new Entity();
                entity.setEntityId(entityId);
                entity.setEntityType(entityType);
                entity.setX(x);
                entity.setY(y);
                entity.setRotation(rotation);
                entity.setSpeed(speed);
                entity.setLife(life);
                entity.setControl(control);




                Entity entity = new Entity();
                entity.setEntityId(in.nextInt());
                entity.setEntityType(in.next());
                entity.setX(in.nextInt());
                entity.setY(in.nextInt());
                entity.setRotation(in.nextInt());
                entity.setSpeed(in.nextInt());
                entity.setLife(in.nextInt());
                entity.setControl(in.nextInt());
                
                controller.addEntity(entity);
            }

System.err.println("--------------------------------------");

            controller.getMyShip();
            controller.getEnShip();
            controller.setCommands();

            controller.setDefineEntities();
            controller.setWhereToGo();
            controller.generateCommands();

            for (int i = 0; i < myShipCount; i++) {

                // Write an action using System.out.println()
                // To debug: System.err.println("Debug messages...");

Random r = new Random();
                int a = r.nextInt(23);
                int b = r.nextInt(21);


                //System.out.println(Action.MOVE + " " + controller.getX() + " " + controller.getY());
                //System.out.println(Action.MOVE + " " + controller.myX + " " + controller.myY);

if (controller.getDis() < 5) {
                    System.out.println(Action.FIRE + " " + controller.getEnX() + " " + controller.getEnY());
                } else{
                    System.out.println(Action.MOVE + " " + controller.getingX() + " " + controller.getingY());
                }


                System.out.println(controller.getCommand(i));


                // Any valid action, such as "WAIT" or "MOVE x y"
            }

            controller.clearData();
        }
    }
}




class Controller{
    private List<Entity> entities = new ArrayList<>();
private int barX = 1000;
    private int barY = 1000;
    private int enX = 1000;
    private int enY = 1000;
    private int barId = 1000;
    private int barClear = 1000;
    private int dist;



private List<Ship> enShips = new ArrayList<>();
    private List<Ship> myShips = new ArrayList<>();

    private List<String> command = new ArrayList<>();

    private DefineEntities defineEntities =  new DefineEntities();
    private List<Move> movement = new ArrayList<>();
    private WhereToGo whereToGo = new WhereToGo();


    private List<Entity> barrels = new ArrayList<>();
    private List<Entity> myShips = new ArrayList<>();

    public void setDefineEntities(){
        defineEntities.defineEntitites2(entities);
        barrels = defineEntities.getBarrels();
        myShips = defineEntities.getMyShips();
    }

    public void setWhereToGo(){
        whereToGo.setBarrels(barrels);
        whereToGo.setMyShips(myShips);
        movement = whereToGo.initializeClosestBarrel();
    }

    public void generateCommands(){
        System.err.println(movement.size());
        for (Move move : movement) {
            command.add("MOVE " + move.getX() + " " + move.getY());
            System.err.println("MOVE " + move.getX() + " " + move.getY());
        }
    }

    public void addEntity(Entity entity){
        entities.add(entity);
    }
    
public void setCommands(){
        for (Ship myShip: myShips) {

            // определить наиболее близкую бочку с ромом
            for (Entity ent : entities){
                if (ent.getEntityType().equals("BARREL")){
                    if ((Math.abs(myShip.getX() - ent.getX()) + Math.abs(myShip.getY() - ent.getY()))
                            < (Math.abs(myShip.getX() - barX) + Math.abs(myShip.getY() - barY))){
                        barX = ent.getX();
                        barY = ent.getY();
                        barId = ent.getEntityId();
                    }
                }
            }

            //удалить данную точку из списка
            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i).getEntityId() == barId) {
                    barClear = i;
                }
            }
            if (barClear < 500){
                entities.remove(barClear);
            }

            //определить наиболее близкого соперника
            for (Ship enShip : enShips) {
                if ((Math.abs(myShip.getX() - enShip.getX()) + Math.abs(myShip.getY() - enShip.getY()))
                        < (Math.abs(myShip.getX() - enX) + Math.abs(myShip.getY() - enY))){
                    enX = enShip.getX();
                    enY = enShip.getY();
                }
            }

            //определить растояние до сопперника
            dist = Math.abs(myShip.getX() - enX) + Math.abs(myShip.getY() - enY);

            //если ближе 5, то стреляем
            if (dist < 5) {
                command.add(Action.FIRE + " " + enX + " " + enY);
            } else{
                command.add(Action.MOVE + " " + barX + " " + barY);
            }

            //сбрасываем все переменные
            barX = 1000;
            barY = 1000;
            enX = 1000;
            enY = 1000;
            barClear = 1000;
            barId = 1000;
        }

    }


    public String getCommand(int i){
        return command.get(i);
    }
    
    
public void getMyShip(){
        for (Entity ent : entities){
            if (ent.getEntityType().equals("SHIP") && ent.getControl() == 1){
                myShips.add(new Ship(ent.getEntityId(), ent.getX(), ent.getY()));
            }
        }
    }

    public void getEnShip(){
        for (Entity ent : entities){
            if (ent.getEntityType().equals("SHIP") && ent.getControl() == 0){
                enShips.add(new Ship(ent.getEntityId(), ent.getX(), ent.getY()));
            }
        }
    }


    public void clearData(){
        entities.clear();
        barrels.clear();
        myShips.clear();
enShips.clear();
        myShips.clear();

        command.clear();

        movement.clear();
    }

}

class Ship{
    private int id;
    private int x;
    private int y;

    public Ship(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

class MovementInitializer{
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> barrels = new ArrayList<>();
    private List<Entity> myShips = new ArrayList<>();
    private List<Entity> enShips = new ArrayList<>();
    private List<Entity> mines = new ArrayList<>();
    private List<Entity> cannonballs = new ArrayList<>();

    DefineEntities defineEntities = new DefineEntities();

    public MovementInitializer() {
        this.defineEntities.defineEntitites2();
    }
}

class WhereToGo {
    Action[] action = Action.values();

    private List<Entity> barrels = new ArrayList<>();
    private List<Entity> myShips = new ArrayList<>();
    private List<Move> movement = new ArrayList<>();

    public void setBarrels(List<Entity> barrels) {
        this.barrels = barrels;
    }

    public void setMyShips(List<Entity> myShips) {
        this.myShips = myShips;
    }

    public List<Move> initializeClosestBarrel(){
        int position = 0;
        for (Entity myShip : myShips) {
            Move move = new Move(myShip.getEntityId(), 1000, 1000);
            movement.add(findClosestBarrel(myShip, move, 0, position));
            position++;
        }

        changeDuplicateBarrels();
        System.err.println(movement.size());
        return movement;
    }

    private Move findClosestBarrel(Entity myShip, Move move, int exceptIdBarrel, int position){
        for (Entity barrel : barrels){
            if ((Math.abs(myShip.getX() - barrel.getX()) + Math.abs(myShip.getY() - barrel.getY()))
                    < (Math.abs(myShip.getX() - move.getX()) + Math.abs(myShip.getY() - move.getY()))
                    && exceptIdBarrel != barrel.getEntityId()){
                move.setX(barrel.getX());
                move.setY(barrel.getY());
                move.setIdBarrel(barrel.getEntityId());
                move.setDistanceBar(Math.abs(myShip.getX() - barrel.getX()) + Math.abs(myShip.getY() - barrel.getY()));
                move.setPositionOnList(position);
            }
        }

        return move;
    }

    private void changeDuplicateBarrels(){
        for (int i = 0; i < movement.size(); i++) {
            for (Move move : movement) {
                if (movement.get(i).getIdShip() != move.getIdShip() &&
                        movement.get(i).getIdBarrel() == move.getIdBarrel()){

                    if (move.getDistanceBar() > movement.get(i).getDistanceBar()){

                        movement.set(i,
                                findClosestBarrel(
                                        myShips.get(move.getPositionOnList()),
                                        new Move(myShips.get(move.getPositionOnList()).getEntityId(), 1000, 1000),
                                        move.getIdBarrel(),
                                        move.getPositionOnList()));
                    }else {

                        movement.set(i,
                                findClosestBarrel(
                                        myShips.get(movement.get(i).getPositionOnList()),
                                        new Move(myShips.get(movement.get(i).getPositionOnList()).getEntityId(), 1000, 1000),
                                        movement.get(i).getIdBarrel(),
                                        movement.get(i).getPositionOnList()));
                    }
                }
            }
        }
    }

}

class Move{
    private int idShip;
    private int positionOnList;
    private int x;
    private int y;
    private int idBarrel;
    private int distanceBar;

    public Move(int idShip, int x, int y) {
        this.idShip = idShip;
        this.x = x;
        this.y = y;
    }

    public int getPositionOnList() {
        return positionOnList;
    }

    public void setPositionOnList(int positionOnList) {
        this.positionOnList = positionOnList;
    }

    public int getDistanceBar() {
        return distanceBar;
    }

    public void setDistanceBar(int distanceBar) {
        this.distanceBar = distanceBar;
    }

    public int getIdBarrel() {
        return idBarrel;
    }

    public void setIdBarrel(int idBarrel) {
        this.idBarrel = idBarrel;
    }

    public int getIdShip() {
        return idShip;
    }

    public void setIdShip(int idShip) {
        this.idShip = idShip;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}


//необхоимы только методы
class DefineEntities{
    private List<Entity> barrels = new ArrayList<>();
    private List<Entity> myShips = new ArrayList<>();
    private List<Entity> enShips = new ArrayList<>();
    private List<Entity> mines = new ArrayList<>();
    private List<Entity> cannonballs = new ArrayList<>();

    public ArrayList<Entity> defineEntitites(ArrayList<Entity> entities, String type){
        ArrayList<Entity> requiredEntity = new ArrayList<>();

        for (Entity entity : entities) {
            if (entity.getEntityType().equals(type)){
                requiredEntity.add(entity);
            }
        }

        return requiredEntity;
    }

    public void defineEntitites2(List<Entity> entities){

        EntityTypes[] entityTypes = EntityTypes.values();

        for (Entity entity : entities) {
            if (entity.getEntityType().equals(entityTypes[0].getType())){
                barrels.add(entity);
            } else if (entity.getEntityType().equals(entityTypes[1].getType()) && entity.getControl() == 1){
                myShips.add(entity);
            } else if (entity.getEntityType().equals(entityTypes[1].getType()) && entity.getControl() == 0){
                enShips.add(entity);
            } else if (entity.getEntityType().equals(entityTypes[2].getType())){
                cannonballs.add(entity);
            } else if (entity.getEntityType().equals(entityTypes[3].getType())){
                mines.add(entity);
            }
        }
    }

    public List<Entity> getBarrels() {
        return barrels;
    }

    public void setBarrels(List<Entity> barrels) {
        this.barrels = barrels;
    }

    public List<Entity> getMyShips() {
        return myShips;
    }

    public void setMyShips(List<Entity> myShips) {
        this.myShips = myShips;
    }

    public List<Entity> getEnShips() {
        return enShips;
    }

    public void setEnShips(List<Entity> enShips) {
        this.enShips = enShips;
    }

    public List<Entity> getMines() {
        return mines;
    }

    public void setMines(List<Entity> mines) {
        this.mines = mines;
    }

    public List<Entity> getCannonballs() {
        return cannonballs;
    }

    public void setCannonballs(List<Entity> cannonballs) {
        this.cannonballs = cannonballs;
    }
}


enum EntityTypes{
    BARREL("BARREL"),
    SHIP("SHIP"),
    CANNONBALL("CANNONBALL"),
    MINE("MINE");

    private String type;

    EntityTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

enum Action{
    MOVE("MOVE"),
    FIRE("FIRE"),
    MINE("MINE"),
    PORT("PORT"),
    STARBOARD("STARBOARD"),
    FASTER("FASTER"),
    SLOWER("SLOWER"),
    WAIT("WAIT");

    private String action;

    Action(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}

class Entity{
       private int entityId;
       private String entityType;
       private int x;
       private int y;
       private int rotation;
       private int speed;
       private int life;
       private int control;

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getControl() {
        return control;
    }

    public void setControl(int control) {
        this.control = control;
    }

}

























*/
