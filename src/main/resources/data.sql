--USERS
insert into users (id, email, first_name, last_name, password)
values ('ba804cb9-fa14-42a5-afaf-be488742fc54', 'admin@example.com', 'James', 'Bond', '$2a$10$U/FW.Fl/bhhiZo6YNwD46O5Fu1JzLDIK1H.Gcb/FNECotkW6Ev50a'),
       ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'user@example.com', 'Tyler', 'Durden', '$2a$10$U/FW.Fl/bhhiZo6YNwD46O5Fu1JzLDIK1H.Gcb/FNECotkW6Ev50a') ON CONFLICT DO NOTHING;


--ROLES
INSERT INTO role(id, name)
VALUES ('d29e709c-0ff1-4f4c-a7ef-09f656c390f1', 'DEFAULT'),
       ('ab505c92-7280-49fd-a7de-258e618df074', 'USER_MODIFY'),
       ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', 'USER_DELETE'),
       ('40684160-1f3c-4fb6-98f4-6f693d01d29e', 'ADMIN')
ON CONFLICT DO NOTHING;

--AUTHORITIES
INSERT INTO authority(id, name)
VALUES ('2ebf301e-6c61-4076-98e3-2a38b31daf86', 'DEFAULT'),
       ('76d2cbf6-5845-470e-ad5f-2edb9e09a868', 'USER_MODIFY'),
       ('21c942db-a275-43f8-bdd6-d048c21bf5ab', 'USER_DELETE'),
       ('7b2e5bcb-93a3-470f-93d8-94ee1fb22962', 'MODIFY_FOREIGN_POST'),
       ('18f2c852-2011-4707-9049-5e2c72800a88', 'DELETE_FOREIGN_POST')
ON CONFLICT DO NOTHING;

--assign roles to users
insert into users_role (users_id, role_id)
values ('ba804cb9-fa14-42a5-afaf-be488742fc54', '40684160-1f3c-4fb6-98f4-6f693d01d29e'),
       ('0d8fa44c-54fd-4cd0-ace9-2a7da57992de', 'd29e709c-0ff1-4f4c-a7ef-09f656c390f1') ON CONFLICT DO NOTHING;

--assign authorities to roles
INSERT INTO role_authority(role_id, authority_id)
VALUES ('d29e709c-0ff1-4f4c-a7ef-09f656c390f1', '2ebf301e-6c61-4076-98e3-2a38b31daf86'),
       ('ab505c92-7280-49fd-a7de-258e618df074', '76d2cbf6-5845-470e-ad5f-2edb9e09a868'),
       ('c6aee32d-8c35-4481-8b3e-a876a39b0c02', '21c942db-a275-43f8-bdd6-d048c21bf5ab'),
       ('40684160-1f3c-4fb6-98f4-6f693d01d29e', '2ebf301e-6c61-4076-98e3-2a38b31daf86'),
       ('40684160-1f3c-4fb6-98f4-6f693d01d29e', '76d2cbf6-5845-470e-ad5f-2edb9e09a868'),
       ('40684160-1f3c-4fb6-98f4-6f693d01d29e', '21c942db-a275-43f8-bdd6-d048c21bf5ab'),
       ('40684160-1f3c-4fb6-98f4-6f693d01d29e', '7b2e5bcb-93a3-470f-93d8-94ee1fb22962'),
       ('40684160-1f3c-4fb6-98f4-6f693d01d29e', '18f2c852-2011-4707-9049-5e2c72800a88')
       ON CONFLICT DO NOTHING;

