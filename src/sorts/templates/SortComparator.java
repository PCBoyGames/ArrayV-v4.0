package sorts.templates;

import java.util.Comparator;

public class SortComparator implements Comparator<Sort> {
    public SortComparator() {}

    @Override
    public int compare(Sort left, Sort right) {
        return left.getSortListName().compareTo(right.getSortListName());
    }
}