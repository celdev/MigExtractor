import java.util.Arrays;
import java.util.List;

abstract class QueryTree {

    private List<Query> queryList;

    public QueryTree(Query... queries) {
        queryList = Arrays.asList(queries);
    }




}
