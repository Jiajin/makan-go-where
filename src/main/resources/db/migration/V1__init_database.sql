create table if not exists user
(
    id_no uuid DEFAULT uuid_generate_v4() NOT NULL,
    name text,
    email text
);

create table if not exists session
(
    id_no uuid DEFAULT uuid_generate_v4() NOT NULL,
    name text,
    created_by text,
    status text,

)