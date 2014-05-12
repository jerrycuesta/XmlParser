package saxfeedparser;

public abstract class FeedParserFactory {
	static String almars = "http://www.marines.mil/DesktopModules/DigArticle/RSS.ashx?portalid=59&moduleid=27141";
	static String maradmins = "http://www.marines.mil/DesktopModules/DigArticle/RSS.ashx?portalid=59&moduleid=27141";
	static String pressreleases = "http://www.marines.mil/DesktopModules/DigArticle/RSS.ashx?portalid=59&moduleid=6974";
	static String ordersanddirectives = "http://www.marines.mil/DesktopModules/DigArticle/RSS.ashx?portalid=59&moduleid=27152";

	// static String colibrirss = "";

	public static FeedParser getParser1() {
		return getParser1(ParserType.ANDROID_SAX);
	}

	public static FeedParser getParser1(ParserType typealmars) {
		return new AndroidSaxFeedParser(ordersanddirectives);
	}

	public static FeedParser getParser2() {
		return getParser2(ParserType.ANDROID_SAX);
	}

	public static FeedParser getParser2(ParserType typealmars) {
		return new AndroidSaxFeedParser(maradmins);
	}

	public static FeedParser getParser3() {
		return getParser3(ParserType.ANDROID_SAX);
	}

	public static FeedParser getParser3(ParserType typealmars) {
		return new AndroidSaxFeedParser(almars);
	}

	public static FeedParser getParser4() {
		return getParser4(ParserType.ANDROID_SAX);
	}

	public static FeedParser getParser4(ParserType typealmars) {
		return new AndroidSaxFeedParser(pressreleases);
	}

}
