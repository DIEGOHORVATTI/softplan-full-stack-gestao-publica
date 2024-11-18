CREATE TABLE IF NOT EXISTS clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    limite INTEGER NOT NULL,
    saldo INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS transacoes (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER NOT NULL,
    valor INTEGER NOT NULL,
    tipo CHAR(1) NOT NULL,
    descricao VARCHAR(10) NOT NULL,
    realizada_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_clientes_transacoes FOREIGN KEY (cliente_id) REFERENCES clientes (id)
);

CREATE INDEX IF NOT EXISTS idx_transacoes_cliente_id_realizada_em ON transacoes (cliente_id, realizada_em DESC);

DO $$
BEGIN
    INSERT INTO clientes (nome, limite)
    VALUES
        ('o barato sai caro', 1000 * 100),
        ('zan corp ltda', 800 * 100),
        ('les cruders', 10000 * 100),
        ('padaria joia de cocaia', 100000 * 100),
        ('kid mais', 5000 * 100);
END; $$