import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        Controller controller = new Controller();

        while (true) {
            int myShipCount = in.nextInt(); // the number of remaining ships
            int entityCount = in.nextInt(); // the number of entities (e.g. ships, mines or cannonballs)
            for (int i = 0; i < entityCount; i++) {

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

            controller.setDefineEntities();

            for (int i = 0; i < myShipCount; i++) {
                System.out.println(controller.getCommand(i));
                //System.out.println("MOVE 7 3");
            }

            controller.clearData();
        }
    }
}


class Controller{
    private List<Entity> entities = new ArrayList<>();

    private List<String> command = new ArrayList<>();


    public void setDefineEntities(){
        MovementInitializer movementInitializer = new MovementInitializer(entities);
        command = movementInitializer.CoordinateMovement();
        //System.err.println(command.get(0));
    }


    public void addEntity(Entity entity){
        entities.add(entity);
    }


    public String getCommand(int i){
        return command.get(i);
    }


    public void clearData(){
        entities.clear();
        command.clear();
    }

}

class MovementInitializer{
    Action[] action = Action.values();

    private List<Entity> entities = new ArrayList<>();
    private List<Entity> barrels = new ArrayList<>();
    private List<Entity> myShips = new ArrayList<>();
    private List<Entity> enShips = new ArrayList<>();
    private List<Entity> mines = new ArrayList<>();
    private List<Entity> cannonballs = new ArrayList<>();

    private DefineEntities defineEntities = new DefineEntities();
    private WhereToGo whereToGo = new WhereToGo();
    private List<Move> movement = new ArrayList<>();


    public MovementInitializer(List<Entity> entities) {
        this.entities = entities;
        this.defineEntities.defineEntitites2(entities);

        barrels = defineEntities.getBarrels();
        myShips = defineEntities.getMyShips();
        enShips = defineEntities.getEnShips();
        mines = defineEntities.getMines();
        cannonballs = defineEntities.getCannonballs();

        whereToGo.setBarrels(barrels);
        whereToGo.setMyShips(myShips);

        movement = whereToGo.initializeClosestBarrel();
    }

    public List<String> CoordinateMovement(){
        for (Move move : movement) {
            System.err.println(move.getxBarrel() + " " + move.getyBarrel() + "\n"
                            + move.getxShip() + " " + move.getyShip());
        }

        Coordinate coordinate = new Coordinate(movement);

        return coordinate.getCoordinatedMovement();
    }

}

class Coordinate{
    private final int[][] DIRECTIONS_EVEN = new int[][] { { 1, 0 }, { 0, -1 }, { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, 1 } };
    private final int[][] DIRECTIONS_ODD = new int[][] { { 1, 0 }, { 1, -1 }, { 0, -1 }, { -1, 0 }, { 0, 1 }, { 1, 1 } };
    public final static int[][] ROTATION_PATH = new int[][] {{0, 1, 2, 3, 2, 1}, {1, 0, 1, 2, 3, 2}, {2, 1, 0, 1, 2, 3},
                                                                {3, 2, 1, 0, 1, 2}, {2, 3, 2, 1, 0, 1}, {1, 2, 3, 2, 1, 0}};
    public final static String[][] ROTATION_COMAND = new String[][] {{"0", "PORT", "PORT", "PORT", "STARBOARD", "STARBOARD"},
                                                                    {"STARBOARD", "0", "PORT", "PORT", "STARBOARD", "STARBOARD"},
                                                                    {"STARBOARD", "STARBOARD", "0", "PORT", "PORT", "PORT"},
                                                                    {"STARBOARD", "STARBOARD", "STARBOARD", "0", "PORT", "PORT"},
                                                                    {"PORT", "PORT", "STARBOARD", "STARBOARD", "0", "PORT"},
                                                                    {"PORT", "PORT", "STARBOARD", "STARBOARD", "STARBOARD", "0"}};
    private final int EVEN = 2;
    private final int ODD = 3;
    private int[][] direction;

    List<Move> movement = new ArrayList<>();

    public Coordinate(List<Move> movement){
        this.movement = movement;
    }

    public List<String> getCoordinatedMovement(){

        List<String> moves = new ArrayList<>();
        calculateCoordinate();

        for (Move move : movement) {
            //System.err.println("Rotation " + move.getRotationCommand());
            //System.err.println("Before " + move.getRotationCommand());
            if ((move.getRotationCommand().equals(Action.PORT.getAction())) || (move.getRotationCommand().equals(Action.STARBOARD.getAction()))){
                //System.err.println("After " + move.getRotationCommand());
                moves.add(move.getRotationCommand());
            } else moves.add("MOVE " + move.getxBarrel() + " " + move.getyBarrel());
            //System.err.println("After " + move.getRotationCommand());
        }

        //System.err.println("After " + moves.get(0));

        return moves;
    }

    private void calculateCoordinate(){
        for (Move move : movement) {
            if (move.getyShip() % 2 == 0 || move.getyShip() == 0) {
                //move = calcEvenOdd(EVEN, move);
                move = calcEven(move);
                move = calcEvenRotation(move);
            }else {
                //move = calcEvenOdd(ODD, move);
                move = calcOdd(move);
                move = calcOddRotation(move);
            }
        }
    }

    private Move calcOdd(Move move){

        /*System.err.println(move.getyShip() + " " + move.getyBarrel());
        System.err.println(move.getxShip() + " " + move.getxBarrel());
        System.err.println("ODD");*/

        if (move.getyShip() < move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_ODD[4][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_ODD[4][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FOUR.getDirection()]);
            }else if (move.getxShip() < move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_ODD[5][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_ODD[5][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FIVE.getDirection()]);
            }else if (move.getxShip() == move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_ODD[4][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_ODD[4][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FOUR.getDirection()]);
            }
        }else if (move.getyShip() == move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_ODD[3][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_ODD[3][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.THREE.getDirection()]);
            }else if (move.getxShip() < move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_ODD[0][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_ODD[0][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.ZERO.getDirection()]);
            }
        }else if (move.getyShip() > move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_ODD[2][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_ODD[2][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.TWO.getDirection()]);
            }else if (move.getxShip() < move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_ODD[1][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_ODD[1][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.ONE.getDirection()]);
            }else if (move.getxShip() == move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_ODD[2][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_ODD[2][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.TWO.getDirection()]);
            }
        }
        return move;
    }

    private Move calcEvenRotation(Move move){

        System.err.println("We go to " + move.getxBarrel() + " " + move.getyBarrel());
        System.err.println("Rotation " + move.getRotation());


        if ((move.getxBarrel() - move.getxShip()) == 1 && (move.getyBarrel() - move.getyShip()) == 0){
            System.err.println("0");
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.ZERO.getDirection()]);
        }else if ((move.getxBarrel() - move.getxShip()) == 0 && (move.getyBarrel() - move.getyShip()) == -1){
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.ONE.getDirection()]);
            System.err.println("1");
        }else if ((move.getxBarrel() - move.getxShip()) == -1 && (move.getyBarrel() - move.getyShip()) == -1){
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.TWO.getDirection()]);
            System.err.println("2 here");
        }else if ((move.getxBarrel() - move.getxShip()) == -1 && (move.getyBarrel() - move.getyShip()) == 0){
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.THREE.getDirection()]);
            System.err.println("3");
        }else if ((move.getxBarrel() - move.getxShip()) == -1 && (move.getyBarrel() - move.getyShip()) == 1){
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FOUR.getDirection()]);
            System.err.println("4");
        }else if ((move.getxBarrel() - move.getxShip()) == 0 && (move.getyBarrel() - move.getyShip()) == 1){
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FIVE.getDirection()]);
            System.err.println("5");
        }

        
        /*if (move.getyShip() < move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FOUR.getDirection()]);
            }else if (move.getxShip() < move.getxBarrel()){
                move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FIVE.getDirection()]);
            }else if (move.getxShip() == move.getxBarrel()){
                move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FOUR.getDirection()]);
            }
        }else if (move.getyShip() == move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.THREE.getDirection()]);
            }else if (move.getxShip() < move.getxBarrel()){
                move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.ZERO.getDirection()]);
            }
        }else if (move.getyShip() > move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.TWO.getDirection()]);
                System.err.println("Here2");
            }else if (move.getxShip() < move.getxBarrel()){
                System.err.println("Here");
                move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.ONE.getDirection()]);
            }else if (move.getxShip() == move.getxBarrel()){
                move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.TWO.getDirection()]);
            }
        }*/

        return move;
    }

    private Move calcOddRotation(Move move){

        System.err.println("We go to " + move.getxBarrel() + " " + move.getyBarrel());
        System.err.println("Rotation " + move.getRotation());


        if ((move.getxBarrel() - move.getxShip()) == 1 && (move.getyBarrel() - move.getyShip()) == 0){
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.ZERO.getDirection()]);
            System.err.println("0");
        }else if ((move.getxBarrel() - move.getxShip()) == 1 && (move.getyBarrel() - move.getyShip()) == -1){
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.ONE.getDirection()]);
            System.err.println("1");
        }else if ((move.getxBarrel() - move.getxShip()) == 0 && (move.getyBarrel() - move.getyShip()) == -1){
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.TWO.getDirection()]);
            System.err.println("2");
        }else if ((move.getxBarrel() - move.getxShip()) == -1 && (move.getyBarrel() - move.getyShip()) == 0){
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.THREE.getDirection()]);
            System.err.println("3");
        }else if ((move.getxBarrel() - move.getxShip()) == 0 && (move.getyBarrel() - move.getyShip()) == 1){
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FOUR.getDirection()]);
            System.err.println("4");
        }else if ((move.getxBarrel() - move.getxShip()) == 1 && (move.getyBarrel() - move.getyShip()) == 1){
            move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FIVE.getDirection()]);
            System.err.println("5");
        }


        return move;
    }

    private Move calcEven(Move move){

        /*System.err.println(move.getyShip() + " " + move.getyBarrel());
        System.err.println(move.getxShip() + " " + move.getxBarrel());*/

        if (move.getyShip() < move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_EVEN[4][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_EVEN[4][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FOUR.getDirection()]);
            }else if (move.getxShip() < move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_EVEN[5][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_EVEN[5][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FIVE.getDirection()]);
            }else if (move.getxShip() == move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_EVEN[5][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_EVEN[5][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.FIVE.getDirection()]);
            }
        }else if (move.getyShip() == move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_EVEN[3][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_EVEN[3][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.THREE.getDirection()]);
            }else if (move.getxShip() < move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_EVEN[0][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_EVEN[0][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.ZERO.getDirection()]);
            }
        }else if (move.getyShip() > move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_EVEN[2][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_EVEN[2][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.TWO.getDirection()]);
            }else if (move.getxShip() < move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_EVEN[1][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_EVEN[1][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.ONE.getDirection()]);
            }else if (move.getxShip() == move.getxBarrel()){
                move.setxBarrel(move.getxShip() + DIRECTIONS_EVEN[1][0]);
                move.setyBarrel(move.getyShip() + DIRECTIONS_EVEN[1][1]);
                //move.setRotationCommand(Coordinate.ROTATION_COMAND[move.getRotation()][Compass.ONE.getDirection()]);
            }
        }
        return move;
    }


    /*private Move calcEvenOdd(int parity, Move move){
        //System.err.println(parity);
        if (parity == EVEN) {
            direction = DIRECTIONS_EVEN;
        } else if (parity == ODD){
            direction = DIRECTIONS_ODD;
            System.err.println("ODD");
        }

        System.err.println(move.getyShip() + " " + move.getyBarrel());
        System.err.println(move.getxShip() + " " + move.getxBarrel());

        if (move.getyShip() < move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setxBarrel(move.getxShip() + direction[4][0]);
                move.setyBarrel(move.getyShip() + direction[4][1]);
            }else if (move.getxShip() < move.getxBarrel()){
                move.setxBarrel(move.getxShip() + direction[5][0]);
                move.setyBarrel(move.getyShip() + direction[5][1]);
            }else if (move.getxShip() == move.getxBarrel()){
                move.setxBarrel(move.getxShip() + direction[4][0]);
                move.setyBarrel(move.getyShip() + direction[4][1]);
            }
        }else if (move.getyShip() == move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setxBarrel(move.getxShip() + direction[3][0]);
                move.setyBarrel(move.getyShip() + direction[3][1]);
                //System.err.println(move.getxShip() + " " + direction[3][0] + " " + move.getyShip() + " " + direction[3][1]);
            }else if (move.getxShip() < move.getxBarrel()){
                move.setxBarrel(move.getxShip() + direction[0][0]);
                move.setyBarrel(move.getyShip() + direction[0][1]);
            }
        }else if (move.getyShip() > move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                move.setxBarrel(move.getxShip() + direction[2][0]);
                move.setyBarrel(move.getyShip() + direction[2][1]);
            }else if (move.getxShip() < move.getxBarrel()){
                move.setxBarrel(move.getxShip() + direction[1][0]);
                move.setyBarrel(move.getyShip() + direction[1][1]);
            }else if (move.getxShip() == move.getxBarrel()){
                move.setxBarrel(move.getxShip() + direction[1][0]);
                move.setyBarrel(move.getyShip() + direction[1][1]);
            }
        }
        return move;
    }*/
}

class WhereToGo {

    private List<Entity> barrels = new ArrayList<>();
    private List<Entity> myShips = new ArrayList<>();
    private List<Move> movement = new ArrayList<>();

    public WhereToGo() {
    }

    public WhereToGo(List<Entity> barrels, List<Entity> myShips) {
        this.barrels = barrels;
        this.myShips = myShips;
    }

    public void setBarrels(List<Entity> barrels) {
        this.barrels = barrels;
    }

    public void setMyShips(List<Entity> myShips) {
        this.myShips = myShips;
    }

    public List<Move> initializeClosestBarrel(){
        int position = 0;
        for (Entity myShip : myShips) {
            Move move = new Move(myShip.getEntityId(), 1000, 1000, myShip.getRotation());
            movement.add(findClosestBarrel(myShip, move, 0, position));
            position++;
        }

        //changeDuplicateBarrels();
        return movement;
    }

    private Move findClosestBarrel(Entity myShip, Move move, int exceptIdBarrel, int position){
        for (Entity barrel : barrels){
            if ((Math.abs(myShip.getX() - barrel.getX()) + Math.abs(myShip.getY() - barrel.getY()) + rotation(move))
                    < (Math.abs(myShip.getX() - move.getxBarrel()) + Math.abs(myShip.getY() - move.getyBarrel()) + rotation(move))
                    && exceptIdBarrel != barrel.getEntityId()){
                move.setxBarrel(barrel.getX());
                move.setyBarrel(barrel.getY());
                //move.setxBarrel(16);
                //move.setyBarrel(8);
                move.setIdBarrel(barrel.getEntityId());
                move.setDistanceBar(Math.abs(myShip.getX() - barrel.getX()) + Math.abs(myShip.getY() - barrel.getY()) + rotation(move));
                move.setPositionOnList(position);
                move.setxShip(myShip.getX());
                move.setyShip(myShip.getY());
            }
        }
        //System.err.println("Rotation " + rotation(move));
        return move;
    }

    private int rotation(Move move){
        int rotation = 0;

        /*for (int i = 0; i < Coordinate.ROTATION_PATH.length ; i++) {
            for (int j = 0; j < Coordinate.ROTATION_PATH.length; j++) {
                System.err.print(Coordinate.ROTATION_PATH[j][i]);
            }
            System.err.println();
        }*/


        /*System.err.println(Coordinate.ROTATION_PATH[move.getRotation()][Compass.ZERO.getDirection()]);
        System.err.println(Coordinate.ROTATION_PATH[move.getRotation()][Compass.ONE.getDirection()]);
        System.err.println(Coordinate.ROTATION_PATH[move.getRotation()][Compass.TWO.getDirection()]);
        System.err.println(Coordinate.ROTATION_PATH[move.getRotation()][Compass.THREE.getDirection()]);
        System.err.println(Coordinate.ROTATION_PATH[move.getRotation()][Compass.FOUR.getDirection()]);
        System.err.println(Coordinate.ROTATION_PATH[move.getRotation()][Compass.FIVE.getDirection()]);*/

        if (move.getyShip() < move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                return Coordinate.ROTATION_PATH[move.getRotation()][Compass.TWO.getDirection()];
            }else if (move.getxShip() < move.getxBarrel()){
                return Coordinate.ROTATION_PATH[move.getRotation()][Compass.ONE.getDirection()];
            }else if (move.getxShip() == move.getxBarrel()){
                return Coordinate.ROTATION_PATH[move.getRotation()][Compass.FIVE.getDirection()] >
                        Coordinate.ROTATION_PATH[move.getRotation()][Compass.FOUR.getDirection()] ?
                        Coordinate.ROTATION_PATH[move.getRotation()][Compass.FIVE.getDirection()] :
                        Coordinate.ROTATION_PATH[move.getRotation()][Compass.FOUR.getDirection()] ;
            }
        }else if (move.getyShip() == move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                return Coordinate.ROTATION_PATH[move.getRotation()][Compass.THREE.getDirection()];
            }else if (move.getxShip() < move.getxBarrel()){
                return Coordinate.ROTATION_PATH[move.getRotation()][Compass.ZERO.getDirection()];
            }
        }else if (move.getyShip() > move.getyBarrel()){
            if (move.getxShip() > move.getxBarrel()){
                return Coordinate.ROTATION_PATH[move.getRotation()][Compass.FOUR.getDirection()];
            }else if (move.getxShip() < move.getxBarrel()){
                return Coordinate.ROTATION_PATH[move.getRotation()][Compass.FIVE.getDirection()];
            }else if (move.getxShip() == move.getxBarrel()){
                return Coordinate.ROTATION_PATH[move.getRotation()][Compass.TWO.getDirection()] >
                        Coordinate.ROTATION_PATH[move.getRotation()][Compass.ONE.getDirection()] ?
                        Coordinate.ROTATION_PATH[move.getRotation()][Compass.TWO.getDirection()] :
                        Coordinate.ROTATION_PATH[move.getRotation()][Compass.ONE.getDirection()] ;
            }
        }
        return rotation;
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
                                        new Move(myShips.get(move.getPositionOnList()).getEntityId(), 1000, 1000, myShips.get(move.getPositionOnList()).getRotation()),
                                        move.getIdBarrel(),
                                        move.getPositionOnList()));
                    }else {

                        movement.set(i,
                                findClosestBarrel(
                                        myShips.get(movement.get(i).getPositionOnList()),
                                        new Move(myShips.get(movement.get(i).getPositionOnList()).getEntityId(), 1000, 1000, myShips.get(movement.get(i).getPositionOnList()).getRotation()),
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
    private int rotation;
    private String rotationCommand;
    private int xShip;
    private int yShip;
    private int xBarrel;
    private int yBarrel;
    private int idBarrel;
    private int distanceBar;

    public Move(int idShip, int xShip, int yShip, int rotation) {
        this.idShip = idShip;
        this.xBarrel = xShip;
        this.yBarrel = yShip;
        this.rotation = rotation;
    }

    public String getRotationCommand() {
        return rotationCommand;
    }

    public void setRotationCommand(String rotationCommand) {
        this.rotationCommand = rotationCommand;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getxShip() {
        return xShip;
    }

    public void setxShip(int xShip) {
        this.xShip = xShip;
    }

    public int getyShip() {
        return yShip;
    }

    public void setyShip(int yShip) {
        this.yShip = yShip;
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

    public int getxBarrel() {
        return xBarrel;
    }

    public void setxBarrel(int xBarrel) {
        this.xBarrel = xBarrel;
    }

    public int getyBarrel() {
        return yBarrel;
    }

    public void setyBarrel(int yBarrel) {
        this.yBarrel = yBarrel;
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

enum Compass{
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    int direction;

    Compass(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
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

























