package io.my.rsocket.dto;

public class ChartResponse {
    private int input;
    private int output;

    public ChartResponse() { }

    public ChartResponse(int input, int output) {
        this.input = input;
        this.output = output;
    }

    public int getOutput() {
        return output;
    }

    public int getInput() {
        return input;
    }

    private String getFormat(int value) {
        return "%3s|%" + value + "s";
    }

    @Override
    public String toString() {
        String graphFormat = getFormat(this.output);
        return String.format(graphFormat, this.input, "X");
    }
}
