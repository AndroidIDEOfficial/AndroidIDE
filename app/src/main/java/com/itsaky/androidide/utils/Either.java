package com.itsaky.androidide.utils;

public class Either<L, R> {

	private L left;
	private R right;

	public Either(L l, R r) {
		this.left = l;
		this.right = r;
	}
	
	public static <L, R> Either<L, R> forLeft(L left) {
		return new Either<L, R>(left, null);
	}
	
	public static <L, R> Either<L, R> forRight(R right) {
		return new Either<L, R>(null, right);
	}

	public L getLeft() {
		return left;
	}

	public R getRight() {
		return right;
	}

	public boolean isLeft() {
		return left != null && right == null;
	}

	public boolean isRight() {
		return left == null && right != null;
	}

	@Override
	public String toString() {
		return "Either(left=" + left.toString() + ", right=" + right.toString() + ")";
	}
}
