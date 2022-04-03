package me.zheal.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.md_5.bungee.api.ChatColor;

public class Colorizer {

	private static final char COLOR_CHAR = '\u00A7';
	public static final String startTag = "&#";
	public static final String endTag = "";
	public static final Pattern HEX_PATTERN = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
	
	public static String colorize(String textToColorize) {
		return ChatColor.translateAlternateColorCodes('&', colorizeHex(textToColorize));
	}
	
	public static List<String> colorize(List<String> textToColorize) {
		return textToColorize.stream().map(Colorizer::colorize).collect(Collectors.toCollection(LinkedList::new));
	}
	
	public static List<String> colorize(String... textToColorize) {
		return colorize(Arrays.asList(textToColorize));
	}
	
	private static String colorizeHex(String message)
	{
		if(message == null || message.isEmpty() || !message.contains("&#")) {
			return message;
		}
		Matcher matcher = HEX_PATTERN.matcher(message);
		StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
		while (matcher.find())
		{
			String group = matcher.group(1);
			matcher.appendReplacement(buffer, COLOR_CHAR + "x"
					+ COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
					+ COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
					+ COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
					);
		}
		return matcher.appendTail(buffer).toString();
	}
	
}
