package com.easyliteorm;


public class RegisteredType <T> {
    private Class<?> clazz;
    private SQLiteType sqliteType;

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }


    public SQLiteType getSqliteType() {
        return sqliteType;
    }

    public void setSqliteType(SQLiteType sqliteType) {
        this.sqliteType = sqliteType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisteredType<?> that = (RegisteredType<?>) o;

        if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) return false;
        return sqliteType == that.sqliteType;

    }

    @Override
    public int hashCode() {
        int result = clazz != null ? clazz.hashCode() : 0;
        result = 31 * result + (sqliteType != null ? sqliteType.hashCode() : 0);
        return result;
    }
}
