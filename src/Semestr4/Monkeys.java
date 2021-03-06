package Semestr4;

public class Monkeys {
    // принцип: стараться обходиться без статических методов.
    //
    public static void main(String[] args) {
        new Monkeys(); //создаем одну программу про обезьянок
    }

    private int bananas = 1_000_000;
    private int total = 0;

    public Monkeys() {
        // здесь основная логика программы

        /*
        // обезьянка ест бананы
        int eaten = 0;
        while (bananas > 0 ) {
            bananas--;
            eaten++;
        }

        // %d - подставить число в десятичной системе счисления, result: 1000000
        // %,d - проще читать большие числа, result: 1 000 000
        System.out.println(String.format("A monkey ate %,d bananas", eaten));
        */

        // Если нужно, чтобы программа одновременно выполняла несколько действий,
        // создаются потоки, по одной на каждое действие.
        // Нам нужно, чтобы две обезьянки ели бананы одновременно, поэтому создадим два потока.

        Object monitor = new Object();

        //Runnable - то произвольный код, у него нету аргументов, не возвращает  результатат
        Runnable monkeyAction = () -> {
            int eaten = 0;
            while (bananas > 0 ) {
                //в скобках указывается любой объект. Он называется монитор.
                // если один поток взял монитор (т. е. вошёл в блок), то другие потоки ждут,
                // когда будет возвращён монитор. В данном случае this - это объект Monkeys
                // synchronized (this) {
                // Или можно создать спец. объект, который нужен только как монитор
                synchronized (monitor) {
                    if (bananas > 0) {// повторная проверка, не съели ли уже банан
                        bananas--; // 1) узнать значение bananas 2) уменьшить 3) записать обратно
                        eaten++;
                    }
                }
            }
            // eaten - эта переменная у каждой обезьянки своя
            // bananas - глобальная переменная
            synchronized (monitor) { //используемглобальную переменную, необходима синхронизация.
                total += eaten;
            }
            System.out.println(String.format("A monkey ate %,d bananas. Eaten: %,d", eaten, total));
        };

        for (int i = 0; i < 10; i++) {
            Thread monkey = new Thread(monkeyAction); // new Thread(() -> {...});
            monkey.start(); // start() запускает действия как новый поток
            // много потоков, результат всегда разный
        }
    }

    // если не синхронизировать работу двух обезьянок, то в начале каждая решит, что бананов 1_000_000,
    // каждая уменьшит количество и запишет это число в переменную(в поле) bananas.
    // Т. е. Бананов 999_999, но каждая обезьяна считает, что она уже съела один банан.

    // участки кода, которык нельзя выполнить одновременно нескольким потокам, надо синхронизировать,
    // т. е. явно указывать, что только один поток может выполнять этот участок кода.
    // Для этого есть ключевое слово synchronized.
    // Программирование с помощью synchronized очень сложно, особенно, если программа большая и потоков много.
    // Это самый низкоуровневый метод программы.

    //Задание: обезьянок 10. И еще одна глобальная переменная total. Каждая
    //обезьянка, после того как съела бананы, добавляет в total, сколько всего
    //она съела бананов, и печатает total на экран. Это нужно, чтобы проверить,
    //что все обезьянки в сумме съели ровно 1 000 000 бананов
}
