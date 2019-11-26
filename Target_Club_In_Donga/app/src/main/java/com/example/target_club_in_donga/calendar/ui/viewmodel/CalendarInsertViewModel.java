package com.example.target_club_in_donga.calendar.ui.viewmodel;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.target_club_in_donga.calendar.data.TSLiveData;
import com.example.target_club_in_donga.calendar.room.CalendarDayDatabase;
import com.example.target_club_in_donga.calendar.room.Todo;
import com.example.target_club_in_donga.calendar.room.TodoDao;
import com.example.target_club_in_donga.calendar.utils.DateFormat;

import java.util.List;

public class CalendarInsertViewModel extends AndroidViewModel {
    //DataBase 생성, 전역으로 뽑아내서 이용
    private CalendarDayDatabase db;

    //원래 pivate 으로 만들어서 getter, setter 해야되지만 귀찮아서... public 으로 만들고 있다.
    public LiveData<List<Todo>> todos;
    private String newTodo;
    private boolean workIsChecked;
    private long time;

    public int mCenterPosition;

    Context context;

    /* Testing Code - begin */
    private TSLiveData<String> mCurrentTodo;

    public TSLiveData<String> getCurrentTodo() {
        if (mCurrentTodo == null) mCurrentTodo = new TSLiveData<>();
        return mCurrentTodo;
    }
    /* Testing Code - end */

    public boolean getWorkIsChecked() {
        return workIsChecked;
    }

    public void setWorkIsChecked(boolean workIsChecked) {
        this.workIsChecked = workIsChecked;
    }


    public String getNewTodo() {
        return newTodo;
    }

    public void setNewTodo(String newTodo) {
        this.newTodo = newTodo;
    }


    // onCreate 같은 함수
    public CalendarInsertViewModel(@NonNull Application application) {
        super(application);

        db = Room.databaseBuilder(application, CalendarDayDatabase.class, "todo-db")
                /*.allowMainThreadQueries()*/.build();

        mCenterPosition = 0;
        todos = getAll();

        RoomDatabase.Callback object;

    }

    //
    public String initDB(Long time) {
        this.time = time;
        return DateFormat.getDate(time, DateFormat.CALENDAR_DAY_FORMAT);
    }

    // DB 에 있는 모든 요소들을 Observe
    private LiveData<List<Todo>> getAll() {
        mCenterPosition = mCenterPosition + 1;
        setNewTodo(getNewTodo());
        setWorkIsChecked(getWorkIsChecked());
        return db.todoDao().getAll();   // 무한루프처럼 계속 돌아가고 있다. -> MainViewModel 에서 todos 으로 고정해준다.
    }

    public String initTime(Context context) {
        this.context = context;
        return DateFormat.getDate(time, DateFormat.CALENDAR_DAY_FORMAT);
    }

    // DB 에 insert
    public void insert(String todo, boolean isChecked) {
        new InsertAsyncTask(db.todoDao()).execute(new Todo(todo, isChecked));
    }

    public void update(int[] index, String todo, boolean isChecked) {
        new DeleteAsyncTask(db.todoDao(), index).execute();
        new InsertAsyncTask(db.todoDao()).execute(new Todo(todo, isChecked));
    }

    public void delete(int[] index) {
        new DeleteAsyncTask(db.todoDao(), index).execute();
    }


    //Background 로 DB 에 insert! parameter ( Params, Progress, Result ), Update, Delete 도 동일한 방법으로 진행하면 된다.
    private static class InsertAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao mTodoDao;

        InsertAsyncTask(TodoDao mTodoDao) {  // Tip... Alt + Insert
            this.mTodoDao = mTodoDao;
        }

        // 소스를 보면 비동기 처리를 해주는 애는 바로 Dao 이다. -> 생성자를 만들어서 Dao 를 받아야 한다.
        @Override   // 꼭 필요한 Method, 여기서 비동기 처리를 해줍니다.
        protected Void doInBackground(Todo... todos) {  // spread 연산자 ... 배열로 담겨서 넘어온다.
            mTodoDao.insert(todos[0]);  // 배열에서 하나만 넘겨주니까
            return null;
        }
    }

    // Delete
    private static class DeleteAsyncTask extends AsyncTask<Todo, Void, Void> {
        private TodoDao mTodoDao;
        int[] index;

        public DeleteAsyncTask(TodoDao todoDao, int[] index) {
            this.mTodoDao = todoDao;
            this.index = index;
        }

        @Override   // 꼭 필요한 Method, 여기서 비동기 처리를 해줍니다.
        protected Void doInBackground(Todo... todos) {
            List<Todo> ArrayTodo = mTodoDao.loadAllByIds(index);
            for (int i = 0; i < ArrayTodo.size(); i++)
                mTodoDao.delete(ArrayTodo.get(i));
            return null;
        }
    }
}
