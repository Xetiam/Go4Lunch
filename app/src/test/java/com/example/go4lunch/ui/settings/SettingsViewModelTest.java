package com.example.go4lunch.ui.settings;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.utils.LiveDataTestUtils;
import com.example.go4lunch.utils.SignOutCallback;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class SettingsViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserRepository mockUserRepository;

    private SettingsViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        viewModel = new SettingsViewModel(mockUserRepository);
    }

    @Test
    public void should_delete_and_signout_current_user() {
        // Given
        Mockito.doAnswer(invocation -> {
            SignOutCallback callback = invocation.getArgument(0);
            callback.signout();
            return null;
        }).when(mockUserRepository).deleteUserFromFirestore(Mockito.any(SignOutCallback.class));

        // When
        viewModel.suppressAccount();

        // Then
        SettingsState state = LiveDataTestUtils.getValueForTesting(viewModel.state);
        assert (state instanceof HasSignOutAndDelete);
        //Mockito.verify(mockStateObserver).onChanged(Mockito.any(HasSignOutAndDelete.class));
    }

    @Test
    public void should_modify_user_name() {
        //Given
        String newUserName = "newUserName";

        //When
        viewModel.modifyUserName(newUserName);

        //Then
        Mockito.verify(mockUserRepository).updateUsername(newUserName);
    }

    @Test
    public void should_not_modify_user_name() {
        //Given
        String newUserName = "";

        //When
        viewModel.modifyUserName(newUserName);

        //Then
        Mockito.verify(mockUserRepository, Mockito.never()).updateUsername(newUserName);
        SettingsState state = LiveDataTestUtils.getValueForTesting(viewModel.state);
        assert (state instanceof NoInputState);
    }
}
