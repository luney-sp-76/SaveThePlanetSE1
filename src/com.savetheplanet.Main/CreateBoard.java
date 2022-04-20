package com.savetheplanet.Main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class CreateBoard {

    public CreateBoard() {}

    static List<Square> createBoard() {

        List<Square> board = new ArrayList<>();

        try {
            List<String> fields = Files.lines(Paths.get("src/com.savetheplanet.Main/fields.txt")).collect(Collectors.toList());
            Stream<String> squares = Files.lines(Paths.get("src/com.savetheplanet.Main/squares.txt"));

            squares.forEach(line -> {
                String[] arr = line.split(",");
                String name = arr[0];
                int field = Integer.parseInt(arr[1]);

                if (field >= 3) {
                    board.add(new FundableSquare(name, field, fields.get(Integer.parseInt(arr[1])).split(",")));
                } else {
                    board.add(new Square(name, field));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return board;
    }
}