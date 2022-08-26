package main.java.duke;

import java.util.Scanner;

public class Duke {

    private TaskList tasks;
    private Parser parser;

    /**
     * Constructor for the Duke class.
     * @param filePath - the name of the file to load from
     */
    public Duke(String filePath) {
        tasks = new TaskList(Storage.load(filePath));
        parser = new Parser();
    }

    /**
     * Runs the main Duke program.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        Ui.setup();
        while (true) {
            try {
                String line = scanner.nextLine();
                System.out.println(Ui.divider());
                int result = this.parser.parse(line, tasks);
                if (result == 1) {
                    break;
                }
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Duke("./data/duke.txt").run();
    }
}
