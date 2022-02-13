import java.util.Locale;

public class Program {
    public static void main(String[] args) {
        WebsiteHandler website = new WebsiteHandler(true); //TODO testing override
        NonogramBot bot = new NonogramBot(website.getSize(),website.getColumnConstraints(), website.getRowConstraints(), website);
        bot.solvePuzzle();
    }

}
