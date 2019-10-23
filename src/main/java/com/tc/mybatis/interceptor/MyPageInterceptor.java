package com.tc.mybatis.interceptor;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * @author taosh
 * @create 2019-10-23 11:49
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class MyPageInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("逻辑分页--->物理分页");

        Object[] args = invocation.getArgs();

        MappedStatement ms = (MappedStatement) args[0];

        BoundSql boundSql = (BoundSql) args[1];

        RowBounds rb = (RowBounds) args[2];

        if( rb == RowBounds.DEFAULT ){
            return invocation.proceed();
        }

        // 将原 RowBounds 参数设为 RowBounds.DEFAULT，关闭 MyBatis 内置的分页机制
        //args[2] = RowBounds.DEFAULT;

        String sql = boundSql.getSql();
        String limitStr = String.format("limit %d,%d", rb.getOffset(), rb.getLimit());
        sql += " "+limitStr;

        SqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), sql, boundSql.getParameterMappings());

        Field field = MappedStatement.class.getDeclaredField("sqlSource");
        field.setAccessible(true);
        field.set(ms, sqlSource);

        //执行拦截方法
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
