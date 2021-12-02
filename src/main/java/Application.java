import org.jsoup.Jsoup;

import java.net.URL;

class Scrap {
    String title;
    String relativePath;
    Boolean isOpen;

    public Scrap(String title, String path, Boolean isOpen) {
        this.title = title;
        this.relativePath = path;
        this.isOpen = isOpen;
    }

    @Override
    public String toString() {
        return "Scrap{" +
                "title='" + title + '\'' +
                ", path=" + relativePath +
                ", isOpen=" + isOpen +
                '}';
    }
}

public class Application {
    public static void main(String[] args) throws Exception {
        // scrap 一覧ページにアクセス
        var document = Jsoup.connect("https://zenn.dev/chanmoro?tab=scraps").get();

        // scrap をパースして Scrap オブジェクトを生成する
        var scrapElements = document.select("div[class^='ScrapRow_container_']");
        var scraps = scrapElements.stream().map((scrapElement -> {
            var scrapLink = scrapElement.selectFirst("a");
            var title = scrapLink.text();
            var scrapPath = scrapLink.attr("href");
            var isOpen = switch (scrapElement.selectFirst("div[class^='ScrapRow_meta_'] *[class^='TinyBadge_badge_']").text()) {
                case "Open" -> true;
                default -> false;
            };
            return new Scrap(title, scrapPath, isOpen);
        }));

        // print
        scraps.forEach(s -> System.out.println(s));
    }
}
