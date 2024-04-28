CREATE TABLE IF NOT EXISTS storages
(
    id                bigint AUTO_INCREMENT not null,
    parent_storage_id bigint                null,
    ext_type          varchar(255)          not null,
    storage_name      varchar(255)          not null,
    storage_file_size bigint                not null,
    created_at        datetime              not null,
    updated_at        datetime              null,
    primary key (id)
);
