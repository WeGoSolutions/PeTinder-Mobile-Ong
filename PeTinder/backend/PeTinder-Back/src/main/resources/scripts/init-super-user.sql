INSERT INTO usuario (id, nome, email, senha, data_nasc)
SELECT
    UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', '')),
    'super',
    'super@admin.com',
    'senhaSuperHash',
    '1999-02-20'
WHERE NOT EXISTS (
    SELECT 1
    FROM usuario
    WHERE id = UNHEX(REPLACE('11111111-1111-1111-1111-111111111111', '-', ''))
);