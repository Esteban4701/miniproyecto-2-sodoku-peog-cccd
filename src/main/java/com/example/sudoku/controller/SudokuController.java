package com.example.sudoku.controller;

import com.example.sudoku.model.SudokuModel;
import com.example.sudoku.view.SceneNavigator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * Controller for the Sudoku game view.
 * Handles user interaction between the view and the model.
 */
public class SudokuController implements ISudokuController {

    @FXML private GridPane gridPane;
    @FXML private Label lblTimer;
    @FXML private Button btn1, btn2, btn3, btn4, btn5, btn6;
    @FXML private Button btnEraser, btnHint;
    @FXML private Label lblAmount1, lblAmount2, lblAmount3, lblAmount4, lblAmount5, lblAmount6;

    /** The game model. */
    private SudokuModel model;

    /** Currently selected number to place (0 = none, -1 = eraser). */
    private int selectedNumber = 0;

    /** Currently selected cell row (-1 = none). */
    private int selectedRow = -1;

    /** Currently selected cell column (-1 = none). */
    private int selectedCol = -1;

    /** 6x6 grid of cell buttons. */
    private final Button[][] cells = new Button[6][6];

    /** Count of how many times each number has been placed on the board. */
    private final int[] numberCount = new int[7];

    /** Timer variables. */
    private int seconds = 0;
    private Timeline timer;

    /**
     * Initializes the controller.
     * Called automatically after the FXML is loaded.
     */
    @FXML
    public void initialize() {
        model = new SudokuModel();
        model.generator(0, 0);
        model.hollower();
        buildGrid();
        updateAmountLabels();
        startTimer();
        setupNumberButtons();
        setupActionButtons();
    }

    /**
     * Builds the 6x6 grid of cell buttons and populates them with the model values.
     * Fixed cells are highlighted in blue and disabled.
     */
    private void buildGrid() {
        int i = 0;
        while (i < 6) {
            int j = 0;
            while (j < 6) {
                Button cell = new Button();
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cell.setFocusTraversable(false);

                int value = model.get(i, j);

                if (value != 0) {
                    cell.setText(String.valueOf(value));
                    cell.setStyle("-fx-background-color: #d0e8ff; -fx-font-weight: bold;");
                    cell.setDisable(true);
                    numberCount[value]++;
                    updateButtonState(value);
                } else {
                    cell.setText("");
                    cell.setStyle("-fx-background-color: white;");
                    final int row = i;
                    final int col = j;
                    cell.setOnAction(e -> onCellClicked(row, col));
                }

                cells[i][j] = cell;
                gridPane.add(cell, j, i);
                j++;
            }
            i++;
        }
    }

    /**
     * Handles a cell click event.
     * Selects the cell and places or erases the active number if one is selected.
     *
     * @param row row index of the clicked cell
     * @param col column index of the clicked cell
     */
    @Override
    public void onCellClicked(int row, int col) {
        if (selectedRow != -1 && selectedCol != -1) {
            restoreCellStyle(selectedRow, selectedCol);
        }
        selectedRow = row;
        selectedCol = col;

        if (selectedNumber == -1) {
            eraseCell(row, col);
        } else if (selectedNumber != 0) {
            placeNumber(row, col, selectedNumber);
        } else {
            cells[row][col].setStyle("-fx-background-color: #fff3cd;");
        }
    }

    /**
     * Places a number in a cell and validates it against the solution.
     * Correct placements flash green then restore to white.
     * Incorrect placements stay red until erased.
     *
     * @param row    row index
     * @param col    column index
     * @param number number to place (1-6)
     */
    @Override
    public void placeNumber(int row, int col, int number) {
        int previous = model.get(row, col);
        if (previous != 0) {
            numberCount[previous]--;
            updateButtonState(previous);
        }

        model.set(row, col, number);
        cells[row][col].setText(String.valueOf(number));
        numberCount[number]++;
        updateAmountLabels();
        updateButtonState(number);

        if (model.isCorrect(row, col, number)) {
            model.removeFromRemoved(row, col);
            cells[row][col].setStyle("-fx-background-color: #d4edda;");
            Timeline flash = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                cells[row][col].setStyle("-fx-background-color: white;");
            }));
            flash.setCycleCount(1);
            flash.play();

            if (model.isSolved()) {
                onPuzzleSolved();
            }
        } else {
            cells[row][col].setStyle("-fx-background-color: #f8d7da; -fx-border-color: red;");
        }

        selectedRow = -1;
        selectedCol = -1;
        selectedNumber = 0;
        highlightSelectedButton(0);
    }

    /**
     * Enables or disables a number button based on its count.
     * Disables the button when the number appears 6 times on the board.
     *
     * @param number number to check (1-6)
     */
    private void updateButtonState(int number) {
        if (number < 1 || number > 6) return;
        Button[] btns = {btn1, btn2, btn3, btn4, btn5, btn6};
        btns[number - 1].setDisable(numberCount[number] >= 6);
    }

    /**
     * Erases the value of the selected editable cell.
     * Restores cell to white and updates counters.
     *
     * @param row row index
     * @param col column index
     */
    @Override
    public void eraseCell(int row, int col) {
        int current = model.get(row, col);
        if (current != 0) {
            numberCount[current]--;
            updateAmountLabels();
            updateButtonState(current);
        }
        model.set(row, col, 0);
        cells[row][col].setText("");
        cells[row][col].setStyle("-fx-background-color: #fff3cd;");
    }

    /**
     * Restores a cell's background based on its current state.
     * Red if wrong, white if empty or correct.
     *
     * @param row row index
     * @param col column index
     */
    private void restoreCellStyle(int row, int col) {
        if (cells[row][col].isDisabled()) {
            return;
        }
        int val = model.get(row, col);
        if (val != 0 && !model.isCorrect(row, col, val)) {
            cells[row][col].setStyle("-fx-background-color: #f8d7da; -fx-border-color: red;");
        } else {
            cells[row][col].setStyle("-fx-background-color: white;");
        }
    }

    /**
     * Sets up the number buttons (1-6) to select the active number.
     */
    private void setupNumberButtons() {
        Button[] btns = {btn1, btn2, btn3, btn4, btn5, btn6};
        int k = 0;
        while (k < 6) {
            final int num = k + 1;
            btns[k].setOnAction(e -> selectNumber(num));
            k++;
        }
    }

    /**
     * Sets up eraser, hint, and back button actions.
     */
    private void setupActionButtons() {
        btnEraser.setOnAction(e -> selectNumber(-1));
        btnHint.setOnAction(e -> onHintRequested());
    }

    /**
     * Selects a number to place or activates the eraser.
     * If the same number is already selected, deselects it.
     * Highlights all cells on the board that already contain that number.
     * If a cell is already selected, places or erases immediately.
     *
     * @param number number to select (1-6), or -1 for eraser
     */
    @Override
    public void selectNumber(int number) {
        if (number >= 1 && number <= 6) {
            int currentInCell = (selectedRow != -1 && selectedCol != -1) ? model.get(selectedRow, selectedCol) : -1;
            if (numberCount[number] >= 6 && currentInCell != number) {
                return;
            }
        }

        if (selectedNumber == number) {
            selectedNumber = 0;
            String base = "-fx-background-radius: 50; -fx-min-height: 40; -fx-min-width: 40; -fx-max-width: 40; -fx-max-height: 40; -fx-border-radius: 50;";
            if (number == -1) {
                btnEraser.setStyle(base);
            } else if (number >= 1 && number <= 6) {
                Button[] btns = {btn1, btn2, btn3, btn4, btn5, btn6};
                btns[number - 1].setStyle(base);
            }
            highlightSameNumbers(0);
            return;
        }

        selectedNumber = number;
        highlightSelectedButton(number);
        highlightSameNumbers(number);

        if (selectedRow != -1 && selectedCol != -1) {
            if (number == -1) {
                eraseCell(selectedRow, selectedCol);
            } else {
                placeNumber(selectedRow, selectedCol, number);
            }
        }
    }

    /**
     * Highlights the active number button and resets the others.
     *
     * @param number currently selected number (-1 for eraser, 0 for none)
     */
    private void highlightSelectedButton(int number) {
        String base = "-fx-background-radius: 50; -fx-min-height: 40; -fx-min-width: 40; -fx-max-width: 40; -fx-max-height: 40; -fx-border-radius: 50;";
        Button[] btns = {btn1, btn2, btn3, btn4, btn5, btn6};

        int k = 0;
        while (k < 6) {
            if (!btns[k].isDisabled()) {
                btns[k].setStyle(base);
            }
            k++;
        }

        btnEraser.setStyle(base);

        if (number == -1) {
            btnEraser.setStyle(base + "-fx-background-color: #ffc107;");
        } else if (number >= 1 && number <= 6) {
            if (!btns[number - 1].isDisabled()) {
                btns[number - 1].setStyle(base + "-fx-background-color: #3D4271; -fx-text-fill: white;");
            }
        }
    }

    /**
     * Handles keyboard input for number placement and erasing.
     * Must be connected to the scene via setOnKeyPressed in Main.
     *
     * @param event the key event
     */
    @Override
    @FXML
    public void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case DIGIT1: if (numberCount[1] < 6) selectNumber(1); break;
            case DIGIT2: if (numberCount[2] < 6) selectNumber(2); break;
            case DIGIT3: if (numberCount[3] < 6) selectNumber(3); break;
            case DIGIT4: if (numberCount[4] < 6) selectNumber(4); break;
            case DIGIT5: if (numberCount[5] < 6) selectNumber(5); break;
            case DIGIT6: if (numberCount[6] < 6) selectNumber(6); break;
            case DELETE:
            case BACK_SPACE: selectNumber(-1); break;
            default: break;
        }
    }

    /**
     * Updates the amount labels below each number button.
     * Shows how many times each number currently appears on the board.
     */
    private void updateAmountLabels() {
        Label[] labels = {lblAmount1, lblAmount2, lblAmount3, lblAmount4, lblAmount5, lblAmount6};
        int k = 0;
        while (k < 6) {
            labels[k].setText(String.valueOf(numberCount[k + 1]));
            k++;
        }
    }

    /**
     * Starts the game timer, updating lblTimer every second.
     */
    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds++;
            int min = seconds / 60;
            int sec = seconds % 60;
            lblTimer.setText(String.format("%02d:%02d", min, sec));
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * Handles the hint button action.
     * Reveals a hint cell from the model.
     */
    @Override
    public void onHintRequested() {
        SudokuModel.Pos pos = model.giveHint();
        if (pos != null) {
            int val = model.get(pos.i, pos.j);
            cells[pos.i][pos.j].setText(String.valueOf(val));
            cells[pos.i][pos.j].setStyle("-fx-background-color: #fff3cd; -fx-border-color: orange;");
            numberCount[val]++;
            updateAmountLabels();
            updateButtonState(val);
            if (numberCount[val] >= 6 && selectedNumber == val) {
                selectedNumber = 0;
                highlightSelectedButton(0);
                highlightSameNumbers(0);
            }
        }
    }

    /**
     * Handles the puzzle solved event.
     * Stops the timer and navigates to the win screen.
     */
    @Override
    public void onPuzzleSolved() {
        timer.stop();
        try {
            SceneNavigator.goToWin(lblTimer.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Highlights all cells on the board that contain the given number.
     * Clears highlights first, then marks matching cells in light purple.
     * Respects red cells (incorrect placements) and fixed cells (blue).
     *
     * @param number number to highlight (1-6), or 0 to just clear
     */
    private void highlightSameNumbers(int number) {
        int i = 0;
        while (i < 6) {
            int j = 0;
            while (j < 6) {
                int val = model.get(i, j);
                if (cells[i][j].isDisabled()) {
                    if (val == number && number != 0) {
                        cells[i][j].setStyle("-fx-background-color: #6D72A8; -fx-font-weight: bold;");
                    } else {
                        cells[i][j].setStyle("-fx-background-color: #d0e8ff; -fx-font-weight: bold;");
                    }
                } else {
                    if (val != 0 && !model.isCorrect(i, j, val)) {
                        cells[i][j].setStyle("-fx-background-color: #f8d7da; -fx-border-color: red;");
                    } else if (val == number && number != 0) {
                        cells[i][j].setStyle("-fx-background-color: #6D72A8;");
                    } else {
                        cells[i][j].setStyle("-fx-background-color: white;");
                    }
                }
                j++;
            }
            i++;
        }
    }
}