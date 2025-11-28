package com.liclist.crawlers.modules.commons.entities;

import io.github.thibaultmeyer.cuid.CUID;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "liclist_biddings_sources")
public class BiddingSource {
    @Id
    private String id;

    @Basic(optional = false)
    private String name;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    @OneToMany(mappedBy = Bidding_.BIDDING_SOURCE)
    private Set<Bidding> biddings;

    public BiddingSource() {}

    public BiddingSource(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public BiddingSource(String name) {
        this(CUID.randomCUID2().toString(), name);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
