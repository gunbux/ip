package duke;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Parser {
    static class EmptyInputException extends RuntimeException {
        public EmptyInputException() {
            super("☹ OOPS!!! The description of a todo cannot be empty.");
        }
    }

    static class InvalidInputException extends RuntimeException {
        public InvalidInputException() {
            super("☹ OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }

    public static void parse(String line, TaskList todolist) {
        switch (line) {
            case "":
                throw new EmptyInputException();

            case "bye":
                System.out.println("See you again!");
                return;

            case "list":
                todolist.printData();
                break;

            case "save":
                Storage.save(todolist);
                break;

            default:
//                  We can't use switch statements since we want regex matching
                if (line.matches("mark \\d+")) {
                    int index = Integer.parseInt(line.split(" ")[1]);
                    if (index > todolist.getLength()) {
                        break;
                    }

                    todolist.mark(index - 1);
                    todolist.printData();
                    break;
                }

                if (line.matches("unmark \\d+")) {
                    int index = Integer.parseInt(line.split(" ")[1]);
                    if (index > todolist.getLength()) {
                        break;
                    }

                    todolist.unmark(index - 1);
                    todolist.printData();
                    break;
                }

                if (line.matches("deadline .* by .*")) {
                    String[] res = line.substring(9).split(" by ");
                    String desc = res[0];
                    String deadline = res[1];

                    todolist.addDeadline(desc, deadline);
                    System.out.println(String.format("Added: %s (by: %s)", desc, deadline));
                    break;
                }

                if (line.matches("deadline .* /by .*")) {
                    String[] res = line.substring(9).split(" /by ");
                    String desc = res[0];
                    LocalDate date = LocalDate.parse(res[1]);

                    todolist.addDeadline(desc, date);
                    System.out.println(String.format("Added: %s (by: %s)", desc, date.format(DateTimeFormatter.ofPattern("MMM d yyyy"))));
                    break;
                }

                if (line.matches("event .* at .*")) {
                    String[] res = line.substring(6).split(" at ");
                    String desc = res[0];
                    String time = res[1];

                    todolist.addEvent(desc, time);
                    System.out.println(String.format("Added: %s (at: %s)", desc, time));
                    break;
                }

                if (line.matches("todo .*")) {
                    line = line.substring(5);
                    todolist.addTask(line);
                    System.out.println("Added: " + line);
                    break;
                }

                if (line.matches("delete \\d+")) {
                    int index = Integer.parseInt(line.split(" ")[1]);
                    if (index > todolist.getLength()) {
                        break;
                    }
                    todolist.delete(index - 1);
                    System.out.println("Noted. I've removed this task");
                    System.out.println(String.format("You now have %s tasks in your list.", todolist.getLength()));
                    break;
                }

                throw new InvalidInputException();
        }
    }
}
