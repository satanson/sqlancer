package sqlancer.mariadb.gen;

import java.util.ArrayList;
import java.util.List;

import sqlancer.Query;
import sqlancer.QueryAdapter;
import sqlancer.Randomly;
import sqlancer.mariadb.MariaDBSchema;
import sqlancer.mariadb.MariaDBSchema.MariaDBColumn;
import sqlancer.mariadb.MariaDBSchema.MariaDBTable;
import sqlancer.sqlite3.gen.SQLite3Common;

public class MariaDBIndexGenerator {
	
	public static Query generate(MariaDBSchema s) {
		List<String> errors = new ArrayList<>();
		StringBuilder sb = new StringBuilder("CREATE ");
		errors.add("Key/Index cannot be defined on a virtual generated column");
		if (Randomly.getBoolean()) {
			errors.add("Duplicate entry");
			errors.add("Key/Index cannot be defined on a virtual generated column");
			sb.append("UNIQUE ");
		}
		sb.append("INDEX ");
		sb.append("i" + SQLite3Common.createColumnName(Randomly.smallNumber()));
		if (Randomly.getBoolean()) {
			sb.append(" USING ");
			sb.append(Randomly.fromOptions("BTREE", "HASH")); // , "RTREE")
		}

		sb.append(" ON ");
		MariaDBTable randomTable = s.getRandomTable();
		sb.append(randomTable.getName());
		sb.append("(");
		List<MariaDBColumn> columns = Randomly.nonEmptySubset(randomTable.getColumns());
		for (int i = 0; i < columns.size(); i++) {
			if (i != 0) {
				sb.append(", ");
			}
			sb.append(columns.get(i).getName());
			if (Randomly.getBoolean()) {
				sb.append(" ");
				sb.append(Randomly.fromOptions("ASC", "DESC"));
			}
		}
		sb.append(")");
//		if (Randomly.getBoolean()) {
//			sb.append(" ALGORITHM=");
//			sb.append(Randomly.fromOptions("DEFAULT", "INPLACE", "COPY", "NOCOPY", "INSTANT"));
//			errors.add("is not supported for this operation");
//		}
		
		return new QueryAdapter(sb.toString(), errors, true);
	}

}