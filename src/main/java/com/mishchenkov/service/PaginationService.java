package com.mishchenkov.service;

import java.util.stream.IntStream;

public class PaginationService {

    /**
     * The method returns range of the url particles. Each String consists from two parts,
     * the first one (&count=) is item count on a page, the second (&position=) is the
     * start position for item visualisation.
     *
     * @param count - item count on for each page
     * @param totalItem - total items
     * @return - range of the url tails
     */
    public String[] getPages(int count, int totalItem) {
        int pages = (int) Math.ceil((float)totalItem / count);

        return IntStream.range(0, pages)
                .mapToObj(i -> String.format("&count=%d&position=%d", count, (i * count)))
                .toArray(String[]::new);
    }

}
