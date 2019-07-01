package task.sixteenth;

import java.util.*;
import java.util.function.Consumer;

public class Assert {

    public static void main(String[] args) {

        String randomFrom = "One";
        String randomTo = "Hundred";
        int randomSalary = 100;

        MailMessage firstMessage = new MailMessage(
                "Robert Howard",
                "H.P. Lovecraft",
                "This \"The Shadow over Innsmouth\" story is real masterpiece, Howard!"
        );

        assert firstMessage.getFrom().equals("Robert Howard") : "Wrong firstMessage from address";
        assert firstMessage.getTo().equals("H.P. Lovecraft") : "Wrong firstMessage to address";
        assert firstMessage.getContent().endsWith("Howard!") : "Wrong firstMessage content ending";

        MailMessage secondMessage = new MailMessage(
                "Jonathan Nolan",
                "Christopher Nolan",
                "Брат, почему все так хвалят только тебя, когда практически все сценарии написал я. Так не честно!"
        );

        MailMessage thirdMessage = new MailMessage(
                "Stephen Hawking",
                "Christopher Nolan",
                "Я так и не понял Интерстеллар."
        );

        List<MailMessage> messages = Arrays.asList(
                firstMessage, secondMessage, thirdMessage
        );

        MailService<String> mailService = new MailService<>();

        messages.stream().forEachOrdered(mailService);

        Map<String, List<String>> mailBox = mailService.getMailBox();

        assert mailBox.get("H.P. Lovecraft").equals(
                Arrays.asList(
                        "This \"The Shadow over Innsmouth\" story is real masterpiece, Howard!"
                )
        ) : "wrong mailService mailbox content (1)";

        assert mailBox.get("Christopher Nolan").equals(
                Arrays.asList(
                        "Брат, почему все так хвалят только тебя, когда практически все сценарии написал я. Так не честно!",
                        "Я так и не понял Интерстеллар."
                )
        ) : "wrong mailService mailbox content (2)";

        assert mailBox.get(randomTo).equals(Collections.<String>emptyList()) : "wrong mailService mailbox content (3)";

        Salary salary1 = new Salary("Facebook", "Mark Zuckerberg", 1);
        Salary salary2 = new Salary("FC Barcelona", "Lionel Messi", Integer.MAX_VALUE);
        Salary salary3 = new Salary(randomFrom, randomTo, randomSalary);

        MailService<Integer> salaryService = new MailService<>();

        Arrays.asList(salary1, salary2, salary3).forEach(salaryService);

        Map<String, List<Integer>> salaries = salaryService.getMailBox();
        assert salaries.get(salary1.getTo()).equals(Arrays.asList(1)) : "wrong salaries mailbox content (1)";
        assert salaries.get(salary2.getTo()).equals(Arrays.asList(Integer.MAX_VALUE)) : "wrong salaries mailbox content (2)";
        assert salaries.get(randomTo).equals(Arrays.asList(randomSalary)) : "wrong salaries mailbox content (3)";
    }

    public interface IMessage<T> {
        String getFrom();

        String getTo();

        T getContent();
    }

    public static class MailMessage implements IMessage<String> {
        private String from;
        private String to;
        private String content;

        public MailMessage(String from, String to, String content) {
            this.from = from;
            this.to = to;
            this.content = content;
        }

        public final String getFrom() {
            return from;
        }

        public final String getTo() {
            return to;
        }

        public final String getContent() {
            return content;
        }
    }

    public static class Salary implements IMessage<Integer> {
        private String from;
        private String to;
        private Integer content;

        public Salary(String from, String to, Integer content) {
            this.from = from;
            this.to = to;
            this.content = content;
        }

        public final String getFrom() {
            return from;
        }

        public final String getTo() {
            return to;
        }

        public final Integer getContent() {
            return content;
        }
    }

    public static class MailService<T> implements Consumer<IMessage<T>> {
        Map<String, List<T>> notification = new HashMap() {
            @Override
            public Object get(Object key) {
                return getOrDefault(key, Collections.<T>emptyList());
            }
        };

        @Override
        public void accept(IMessage<T> message) {
            if (notification.containsKey(message.getTo())) {
                List<T> list;
                list = notification.get(message.getTo());
                list.add(message.getContent());
                notification.put(message.getTo(), list);
            } else {
                List<T> list;
                list = new ArrayList<>();
                list.add(message.getContent());
                notification.put(message.getTo(), list);
            }
        }

        public Map<String, List<T>> getMailBox() {
            return notification;
        }
    }
}
