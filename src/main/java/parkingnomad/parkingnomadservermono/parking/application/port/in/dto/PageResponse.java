package parkingnomad.parkingnomadservermono.parking.application.port.in.dto;

import org.springframework.data.domain.Slice;

import java.util.List;

public class PageResponse<T> {
    private final boolean hasNext;

    private final List<T> contents;

    private final int contentsSize;

    private final int currentPage;

    private PageResponse(final boolean hasNext, final List<T> contents, final int contentsSize, final int currentPage) {
        this.hasNext = hasNext;
        this.contents = contents;
        this.contentsSize = contentsSize;
        this.currentPage = currentPage;
    }

    public static <E,O> PageResponse<O> of(final Slice<E> slice, final List<O> contents) {
        System.out.println("adsfadsfads!! : " + slice.hasNext());
        boolean hasNext = slice.hasNext();
        int contentsSize = slice.getSize();
        int currentPage = slice.getNumber();
        return new PageResponse<>(hasNext, contents, contentsSize, currentPage);
    }

    public boolean getHasNext() {
        return hasNext;
    }

    public List<T> getContents() {
        return contents;
    }

    public int getContentsSize() {
        return contentsSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
