package parkingnomad.parkingnomadservermono.member.domain;


import java.time.LocalDateTime;

public class Member {
    private Long id;
    private String sub;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Member(final Long id, final String sub, final String name, final LocalDateTime createdAt, final LocalDateTime updatedAt) {
        this.id = id;
        this.sub = sub;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Member createWithId(
            final Long id,
            final String sub,
            final String name,
            final LocalDateTime createdAt,
            final LocalDateTime updatedAt
    ) {
        return new Member(id, sub, name, createdAt, updatedAt);
    }

    public static Member createWithoutId(final String sub, final String name) {
        return new Member(null, sub, name, null, null);
    }

    public Long getId() {
        return id;
    }

    public String getSub() {
        return sub;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
