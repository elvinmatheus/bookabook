package br.ufc.tpii.model;

import java.time.LocalDateTime;

public class Relatorio<T> {
    
    private LocalDateTime reportDate; // data em que o registro foi criado
    private T report;
    private Reports.Type type;
    Object[] data;
    public Relatorio(T report, Reports.Type type, Object... data) {  
        this.reportDate = LocalDateTime.now();
        this.report = report;
        this.type = type;
        this.data = data;
    }
    public T getReport() { return this.report; }
    public LocalDateTime getDate() { return this.reportDate; }
    public Reports.Type getType() { return this.type; }
    public Object[] getData() { return this.data; }
}
