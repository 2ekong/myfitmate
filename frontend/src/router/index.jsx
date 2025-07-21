import { createBrowserRouter } from 'react-router-dom';
import App from '../App';
import Login from '../pages/Login';
import Signup from '../pages/Signup';
import MyPage from '../pages/MyPage';
import MainPage from '../pages/MainPage';
import EditProfile from '../pages/EditProfile';

import ExerciseAdd from '../pages/Exercise/ExerciseAdd';
import ExerciseLogList from '../pages/Exercise/ExerciseLogList';
import ExerciseEdit from '../pages/Exercise/ExerciseEdit';

import MealAddPage from '../pages/Meals/MealAddPage';
import MealEditPage from '../pages/Meals/MealEditPage';
import MealListPage from '../pages/Meals/MealListPage';
import MealDetailPage from '../pages/Meals/MealDetailPage';

import FoodListPage from '../pages/Foods/FoodListPage';
import FoodEditPage from '../pages/Foods/FoodEditPage';
import FoodDetailPage from '../pages/Foods/FoodDetailPage';
import FoodRegisterPage from '../pages/Foods/FoodRegisterPage';
import FoodCsvSearchPage from '../pages/Foods/FoodCsvSearchPage';

import KakaoCallback from '../pages/KakaoCallback';
import AiCoachPage from '../pages/AiCoachPage';



const router = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      { index: true, element: <MainPage /> },
      { path: 'login', element: <Login /> },
      { path: 'signup', element: <Signup /> },
      { path: 'mypage', element: <MyPage /> },
      { path: 'mypage/edit', element: <EditProfile /> },

      // 운동
      { path: 'exercise/add', element: <ExerciseAdd /> },
      { path: 'exercise/log', element: <ExerciseLogList /> },
      { path: 'exercise/edit/:logId', element: <ExerciseEdit /> },

      // 식단
      { path: 'meals', element: <MealListPage /> },
      { path: 'meals/add', element: <MealAddPage /> },
      { path: 'meals/:id', element: <MealDetailPage /> },
      { path: 'meals/edit/:id', element: <MealEditPage /> },

      // 음식
      { path: 'foods', element: <FoodListPage /> },
      { path: 'foods/register', element: <FoodRegisterPage /> },
      { path: 'foods/:id', element: <FoodDetailPage /> },
      { path: 'foods/edit/:id', element: <FoodEditPage /> },
      { path: 'foods/csv', element: <FoodCsvSearchPage /> },

      // 카카오
      { path: 'kakao/callback', element: <KakaoCallback /> },

      //AI
      { path: 'ai-coach', element: <AiCoachPage /> },

    ],
  },
]);

export default router;
