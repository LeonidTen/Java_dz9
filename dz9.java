/* Некоторые стандартные интерфейсы Java и примеры их использования
Создать класс координат и хранить координаты уже в этом классе. Начать реализацию метода step для лучников.
1. Проверить что сам лучник жив и у него есть стрелы. Если да продолжаем работу иначе return.
2. Найти ближайшего противника и вывести в консоль его имя.*/

//начнем реализацию класса координат. Он будет иметь два поля - x и y, которые будут хранить координаты точки на плоскости.

public class Coords {
    private int x;
    private int y;

    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
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

/*Теперь, когда у нас есть класс координат, мы можем перейти к реализации метода step для лучников. В методе step мы будем искать ближайшего противника и выводить его имя в консоль. 
Для этого нам понадобится информация о местоположении всех юнитов на карте.
Предположим, что у нас есть класс Unit, который представляет собой абстрактный класс для всех юнитов на карте. У каждого юнита есть имя, координаты и количество жизней.*/

public abstract class Unit {
    private String name;
    private Coords coords;
    private int health;

    public Unit(String name, Coords coords, int health) {
        this.name = name;
        this.coords = coords;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coords getCoords() {
        return coords;
    }

    public void setCoords(Coords coords) {
        this.coords = coords;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}


//Теперь мы можем создать класс Archer, который будет наследоваться от класса Unit и иметь дополнительное поле - количество стрел.

public class Archer extends Unit {
    private int arrowsCount;

    public Archer(String name, Coords coords, int health, int arrowsCount) {
        super(name, coords, health);
        this.arrowsCount = arrowsCount;
    }

    public int getArrowsCount() {
        return arrowsCount;
    }

    public void setArrowsCount(int arrowsCount) {
        this.arrowsCount = arrowsCount;
    }

    public void step() {
        if (getHealth() <= 0 || arrowsCount <= 0) {
            return;
        }

        Unit nearestEnemy = findNearestEnemy();
        if (nearestEnemy != null) {
            System.out.println("Nearest enemy: " + nearestEnemy.getName());
        }
    }

    private Unit findNearestEnemy() {       //В методе step мы проверяем, жив ли лучник и есть ли у него стрелы. Если да, то мы вызываем метод findNearestEnemy, который будет искать ближайшего противника
        Unit nearestEnemy = null;
        double minDistance = Double.MAX_VALUE;
        Coords myCoords = getCoords();
    
        // перебираем все юниты на карте и ищем ближайшего противника
        for (Unit unit : unitsOnMap) {
            if (unit instanceof Archer) { // пропускаем других лучников
                continue;
            }
    
            Coords enemyCoords = unit.getCoords();
            double distance = Math.sqrt(Math.pow(enemyCoords.getX() - myCoords.getX(), 2)       //Для реализации метода findNearestEnemy мы можем использовать методы класса Math для вычисления расстояния между двумя точками на плоскости. Например, для вычисления расстояния между точками (x1, y1) и (x2, y2) мы можем использовать формулу:
                                      + Math.pow(enemyCoords.getY() - myCoords.getY(), 2));
            if (distance < minDistance) {
                nearestEnemy = unit;
                minDistance = distance;
            }
        }
    
        return nearestEnemy;
    }
    
}
/*Мы проходимся циклом по всем юнитам на карте и ищем юнита с наименьшим расстоянием до нашего лучника. Мы также проверяем, что найденный юнит не является другим лучником, чтобы не атаковать своих.
Теперь у нас есть метод step для лучников, который находит ближайшего противника и выводит его имя в консоль. Этот метод можно вызывать в основной программе для выполнения действий лучников на каждом ходу игры. */

//Далее, после того как мы нашли ближайшего противника, мы можем направить своего лучника к нему и атаковать его. Например, мы можем реализовать метод attackEnemy следующим образом:

private void attackEnemy(Unit enemy) {
    if (isAlive() && arrows > 0) { // проверяем, что лучник жив и есть стрелы
        Coords myCoords = getCoords();
        Coords enemyCoords = enemy.getCoords();
        double distance = Math.sqrt(Math.pow(enemyCoords.getX() - myCoords.getX(), 2)
                                  + Math.pow(enemyCoords.getY() - myCoords.getY(), 2));
        if (distance <= range) { // проверяем, что противник в зоне поражения
            arrows--;
            enemy.takeDamage(damage);
            System.out.println("Лучник " + getName() + " атакует " + enemy.getName()
                               + " и наносит " + damage + " урона");
        } else {
            System.out.println("Лучник " + getName() + " не может атаковать " + enemy.getName()
                               + ", так как противник слишком далеко");
        }
    }
}
/*Мы сначала проверяем, что лучник жив и у него есть стрелы. Затем мы вычисляем расстояние до противника и проверяем, что он находится в зоне поражения лучника.
Если да, мы уменьшаем количество стрел и наносим урон противнику. Если противник слишком далеко, мы выводим сообщение об ошибке.
Теперь мы можем вызвать метод findNearestEnemy в методе step и атаковать найденного противника: */

public void step() {
    Unit nearestEnemy = findNearestEnemy();
    if (nearestEnemy != null) { // если есть противник
        attackEnemy(nearestEnemy);
    }
}
//Метод step сначала находит ближайшего противника с помощью метода findNearestEnemy, а затем вызывает метод attackEnemy для атаки найденного противника. 
//Если ближайший противник не найден, метод ничего не делает.