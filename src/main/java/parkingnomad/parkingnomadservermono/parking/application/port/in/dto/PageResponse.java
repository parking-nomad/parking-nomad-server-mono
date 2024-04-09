package parkingnomad.parkingnomadservermono.parking.application.port.in.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Slice;

import java.util.List;

@Schema(description = "페이징 응답 형식")
public class PageResponse<T> {

    @Schema(description = "다음 페이지의 존재 유무")
    private final boolean hasNext;

    @Schema(description = "콘텐츠 리스트")
    private final List<T> contents;

    @Schema(description = "콘텐츠의 갯수")
    private final int contentsSize;

    @Schema(description = "현재 페이지 번호")
    private final int currentPage;

    private PageResponse(final boolean hasNext, final List<T> contents, final int contentsSize, final int currentPage) {
        this.hasNext = hasNext;
        this.contents = contents;
        this.contentsSize = contentsSize;
        this.currentPage = currentPage;
    }

    public static <E,O> PageResponse<O> of(final Slice<E> slice, final List<O> contents) {
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
