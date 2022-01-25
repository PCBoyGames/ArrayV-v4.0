package utils;

final class Range {
    public int start;
    public int end;

    public Range(int start1, int end1) {
        this.start = start1;
        this.end = end1;
    }

    public Range() {
        this.start = 0;
        this.end = 0;
    }

    void set(int start1, int end1) {
        this.start = start1;
        this.end = end1;
    }

    int length() {
        return this.end - this.start;
    }
}