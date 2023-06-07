package com.example.go4lunch.ui.login;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.go4lunch.data.user.UserRepository;
import com.example.go4lunch.utils.DataUtils;
import com.example.go4lunch.utils.LiveDataTestUtils;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.firebase.ui.auth.data.model.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class LoginViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private UserRepository mockUserRepository;

    private LoginViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        viewModel = new LoginViewModel(mockUserRepository);
    }

    @Test
    public void should_init_ui_without_user_connected() {
        //Given
        when(mockUserRepository.getCurrentUser()).thenReturn(null);

        //When
        viewModel.initUi();

        //Then
        UserAlreadySignIn state = (UserAlreadySignIn) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertFalse(state.isConnected());
    }

    @Test
    public void should_init_ui_with_user_connected() {
        //Given
        when(mockUserRepository.getCurrentUser()).thenReturn(DataUtils.getDefaultCurrentUser());

        //When
        viewModel.initUi();

        //Then
        UserAlreadySignIn state = (UserAlreadySignIn) LiveDataTestUtils.getValueForTesting(viewModel.state);
        assertTrue(state.isConnected());
    }

    @Test
    public void should_new_user_connect() {
        //Given
        User user = new User.Builder("providerId", "jhondoe@gmail.com")
                .setName("John Doe")
                .setPhotoUri(null)
                .build();
        IdpResponse mockIdpResponse = Mockito.mock(IdpResponse.class);
        when(mockIdpResponse.isSuccessful()).thenReturn(true);
        when(mockIdpResponse.getUser()).thenReturn(user);

        FirebaseAuthUIAuthenticationResult mockResult = Mockito.mock(FirebaseAuthUIAuthenticationResult.class);
        when(mockResult.getIdpResponse()).thenReturn(mockIdpResponse);

        viewModel.onSignInResult(mockResult);
        viewModel.createUser();

        //Then
        LoginViewState state = LiveDataTestUtils.getValueForTesting(viewModel.state);
        assert (state instanceof OnSignInSuccess);
        verify(mockUserRepository).createUser();
    }

    @Test
    public void should_new_user_not_connect() {
        //Given
        User user = new User.Builder("providerId", "jhondoe@gmail.com")
                .setName("John Doe")
                .setPhotoUri(null)
                .build();
        IdpResponse mockIdpResponse = Mockito.mock(IdpResponse.class);
        when(mockIdpResponse.isSuccessful()).thenReturn(false);
        when(mockIdpResponse.getUser()).thenReturn(user);

        FirebaseAuthUIAuthenticationResult mockResult = Mockito.mock(FirebaseAuthUIAuthenticationResult.class);
        when(mockResult.getIdpResponse()).thenReturn(mockIdpResponse);

        viewModel.onSignInResult(mockResult);

        //Then
        LoginViewState state = LiveDataTestUtils.getValueForTesting(viewModel.state);
        assert (state instanceof OnSignInFailure);
    }


}
