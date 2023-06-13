DROP TABLE IF EXISTS STORAGES

create table storages
(
    id                bigint       not null,
    parent_storage_id bigint,
    ext_type          tinyint      not null,
    storage_name      varchar(255) not null,
    storage_file_size bigint       not null,
    created_at        datetime     not null,
    updated_at        datetime     not null,
    primary key (id)
)
