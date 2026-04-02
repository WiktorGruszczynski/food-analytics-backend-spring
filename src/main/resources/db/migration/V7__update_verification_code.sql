ALTER TABLE verification_codes
    ADD type VARCHAR(255);

ALTER TABLE verification_codes
    ADD CONSTRAINT chk_verification_type
    CHECK ( type IN ('PASSWORD_RESET', 'EMAIL_VERIFICATION'));