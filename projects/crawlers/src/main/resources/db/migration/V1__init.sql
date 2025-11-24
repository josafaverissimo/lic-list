create table liclist_biddings_sources (
  id varchar(100) PRIMARY KEY,
  name varchar(50) NOT NULL UNIQUE,
  created_at TIMESTAMPTZ(3) NOT NULL DEFAULT NOW()
);

create table liclist_biddings (
  id varchar(100) PRIMARY KEY,
  code varchar(200) NOT NULL UNIQUE,
  bidding_source_id varchar(100) NOT NULL,
  description TEXT NOT NULL,
  amount bigint,
  amount_scale smallint,
  created_at TIMESTAMPTZ(3) NOT NULL DEFAULT NOW(),

  FOREIGN KEY (bidding_source_id)
    REFERENCES liclist_biddings_sources(id)
    ON DELETE RESTRICT
);

create table liclist_biddings_items (
  id varchar(100) PRIMARY KEY,
  code varchar(100) NOT NULL,
  description TEXT NOT NULL,
  bidding_id varchar(100) NOT NULL,
  quantity int NOT NULL,
  unit_amount bigint NOT NULL,
  unit_amount_scale smallint NOT NULL,
  metric_unit varchar(50) NOT NULL,

  FOREIGN KEY (bidding_id)
    REFERENCES liclist_biddings(id)
    ON DELETE RESTRICT
);
