import org.jsoup.Jsoup;

record Scrap(String title, String relativePath, Boolean isOpen) {
    // Scrap 1記事の情報を保持するクラス
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
        scraps.forEach(System.out::println);
    }
}
