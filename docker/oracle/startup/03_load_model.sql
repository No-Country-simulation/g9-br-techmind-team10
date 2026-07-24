ALTER SESSION SET CONTAINER = FREEPDB1;

CONNECT TECHMIND_APP/"TechmindApp123"@FREEPDB1

BEGIN
    DBMS_VECTOR.LOAD_ONNX_MODEL(
        directory  => 'ONNX_DIR',
        file_name  => 'multilingual-e5-base.onnx',
        model_name => 'MULTILINGUAL_EMBED'
    );
END;
/