package ro.ubbcluj.cs.exam;

import ro.ubbcluj.cs.exam.domain.Item;

public interface MyCallback {
  void add(Item item);
  void showError(String message);
  void clear();
}
