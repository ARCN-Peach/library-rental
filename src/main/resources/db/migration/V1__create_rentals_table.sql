create table rentals (
    id uuid primary key,
    user_id uuid not null,
    book_id uuid not null,
    rented_at timestamptz not null,
    due_at timestamptz not null,
    returned_at timestamptz null,
    status varchar(32) not null
);

create index idx_rentals_user_id on rentals (user_id);
create index idx_rentals_book_id on rentals (book_id);
create index idx_rentals_status on rentals (status);