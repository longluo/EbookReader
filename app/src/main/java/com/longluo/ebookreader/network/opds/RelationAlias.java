package com.longluo.ebookreader.network.opds;

class RelationAlias implements Comparable<RelationAlias> {
	final String Alias;
	final String Type;

	// `alias` and `type` parameters must be either null or interned String.
	RelationAlias(String alias, String type) {
		Alias = alias;
		Type = type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof RelationAlias)) {
			return false;
		}
		RelationAlias r = (RelationAlias) o;
		return Alias == r.Alias && Type == r.Type;
	}

	@Override
	public int hashCode() {
		return (Alias == null ? 0 : Alias.hashCode()) +
			(Type == null ? 0 : Type.hashCode());
	}

	public int compareTo(RelationAlias r) {
		if (Alias != r.Alias) {
			if (Alias == null) {
				return -1;
			} else if (r.Alias == null) {
				return 1;
			}
			return Alias.compareTo(r.Alias);
		}
		if (Type != r.Type) {
			if (Type == null) {
				return -1;
			} else if (r.Type == null) {
				return 1;
			}
			return Type.compareTo(r.Type);
		}
		return 0;
	}

	@Override
	public String toString() {
		return "Alias(" + Alias + "; " + Type + ")";
	}
}

