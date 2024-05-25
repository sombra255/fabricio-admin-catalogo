package com.fabricio.admin.catalogo.application.castmember.update;

import com.fabricio.admin.catalogo.application.UseCaseTest;
import com.fabricio.admin.catalogo.domain.Fixture;
import com.fabricio.admin.catalogo.domain.castmember.CastMember;
import com.fabricio.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fabricio.admin.catalogo.domain.castmember.CastMemberID;
import com.fabricio.admin.catalogo.domain.castmember.CastMemberType;
import com.fabricio.admin.catalogo.domain.exceptions.NotFoundException;
import com.fabricio.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;
    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        Mockito.reset(castMemberGateway);
        //given
        final var aMember =
                CastMember.newMember("cast", CastMemberType.DIRECTOR);

        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;
        final var expectedId = aMember.getId();

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(eq(expectedId))).thenReturn(Optional.of(CastMember.with(aMember)));

        when(castMemberGateway.update(any()))
                .thenAnswer(returnsFirstArg());
        //when

        final var actualOutput = useCase.execute(aCommand);

        //then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(castMemberGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(castMemberGateway, times(1)).update(argThat(
                aUpdatedCastMember ->
                        Objects.equals(expectedName, aUpdatedCastMember.getName())
                                && Objects.equals(expectedId, aUpdatedCastMember.getId())
                                && Objects.equals(expectedType, aUpdatedCastMember.getType())
                                && Objects.equals(aMember.getCreatedAt(), aUpdatedCastMember.getCreatedAt())
                                && aMember.getUpdatedAt().isBefore(aUpdatedCastMember.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        //given
        final var aMember =
                CastMember.newMember("cast", CastMemberType.DIRECTOR);

        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedId = aMember.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(eq(expectedId))).thenReturn(Optional.of(CastMember.with(aMember)));

        //when

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        //given
        final var aMember =
                CastMember.newMember("cast", CastMemberType.DIRECTOR);

        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;
        final var expectedId = aMember.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(eq(expectedId))).thenReturn(Optional.of(CastMember.with(aMember)));

        //when

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(castMemberGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        //given
        final var aMember =
                CastMember.newMember("cast", CastMemberType.DIRECTOR);

        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();
        final var expectedId = CastMemberID.from("123");
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(eq(expectedId))).thenReturn(Optional.empty());

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway, times(1)).findById(eq(expectedId));

        Mockito.verify(castMemberGateway, times(0)).update(any());
    }
}
