package com.tc.mybatis.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import sun.plugin2.main.server.ResultHandler;

import java.sql.Statement;
import java.util.Properties;

/**
 * @author taosh
 * @create 2019-10-23 11:36
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class})})
public class SQLInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long start = System.currentTimeMillis();
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        String method = invocation.getMethod().getName();

        System.out.println("执行方法:"+ method +",获取到的sql语句为:"+sql);

        try {
            return invocation.proceed();
        }finally {
            long end = System.currentTimeMillis();
            System.out.println("SQL执行耗时:"+(end-start)+"ms");
        }

    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        String pors = properties.getProperty("tc");
    }
}
