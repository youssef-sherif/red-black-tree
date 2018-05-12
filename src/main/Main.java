package main;

import static tree.RedBlackTreeUtility.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import tree.RedBlackTree;

public class Main {

	private static Scanner scanner;

	public static void main(String[] args) {

		RedBlackTree tree = new RedBlackTree();
		scanner = new Scanner(System.in);
		Map<String, String> commandsList = new HashMap<>();

		commandsList.put("load", "load a .txt file containing a list of words");
		commandsList.put("insert", "insert a word to dictionary if it is not already in the dictionary");
		commandsList.put("size", "current size of your dictionary");
		commandsList.put("exists", "check if a word exists in the dictionary");
		commandsList.put("delete", "delete word by name (there is no undo)");

		System.out.println("Welcome To Our Simple Dictionary");
		System.out.println("\nAvailable Commands: ");

		commandsList.entrySet().stream().forEach(e -> {
			System.out.printf(" \t %10s\n", e.toString().replaceAll("[=]", "\t\t"));
		});

		System.out.println("\ntype \"exit\" to close the dictionary\n");

		String command;
		do {
			System.out.print(">");
			command = scanner.nextLine();

			if (command.matches("load (.*).txt")) {
				String filename = command.split(" ")[1];
				try {
					load(tree, filename);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}

			} else if (command.matches("insert ([a-z]|[A-Z]|[-])+")) {
				String word = command.split(" ")[1];
				try {
					tree.insert(word);
				} catch (Exception e) {
					System.err.println("ERROR: " + e.getMessage());				
					continue;
				}
				System.out.println("OK");

			} else if (command.matches("delete ([a-z]|[A-Z]|[-])+")) {
				String word = command.split(" ")[1];
				try {
					tree.delete(word);
				} catch (Exception e) {
					System.err.println("ERROR: " + e.getMessage());					
					continue;
				}
				System.out.println("OK");

			} else if (command.matches("size")) {
				System.out.println(tree.getSize());
			} else if (command.matches("exists ([a-z]|[A-Z]|[-])+")) {
				String word = command.split(" ")[1];
				if (tree.Search(word))
					System.out.println("YES");
				else
					System.out.println("NO");

			} else if(command.equals("print")) {
				print(tree.getRoot());
			} else if(command.equals("height")) {
				System.out.println(tree.getHeight());
			}
			else {
				if (!command.matches("exit"))
					System.out.println("ERROR: UNKOWN COMMAND");
			}
		} while (!command.equals("exit"));

		System.out.println("bye");
	}

	public static void load(RedBlackTree tree, String filename) throws Exception {

		BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
		boolean atleast1 = false;
		String word = reader.readLine();
		while (word != null) {
			System.out.println("...");
			try {
				tree.insert(word);
				atleast1 = true;
				System.out.println("INSERT " + word + " SUCCESSFUL");
			} catch (Exception e) {				
				System.out.println(word + " IS DUPLICATE");
			}
			word = reader.readLine();
		}
		if (!atleast1) {
			System.out.println("NO CHANGE");
		}
	}
}
