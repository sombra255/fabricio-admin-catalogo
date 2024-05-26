package com.fabricio.admin.catalogo.infrastructure.castmember;

import com.fabricio.admin.catalogo.MySQLGatewayTest;
import com.fabricio.admin.catalogo.domain.Fixture;
import com.fabricio.admin.catalogo.domain.castmember.CastMember;
import com.fabricio.admin.catalogo.domain.castmember.CastMemberID;
import com.fabricio.admin.catalogo.domain.castmember.CastMemberType;
import com.fabricio.admin.catalogo.domain.pagination.SearchQuery;
import com.fabricio.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fabricio.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway castMemberGateway;
    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    void testDependencies() {
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(castMemberRepository);
    }

    @Test
    public void givenAValidCastMember_whenCallsCreateCastMember_shouldPersistCastMember() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);

        final var expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        // when
        final var actualMember = castMemberGateway.create(CastMember.with(aMember));

        // then
        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), persistedMember.getUpdatedAt());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdate_shouldPersistCastMemberUpdated() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        Assertions.assertEquals(1, castMemberRepository.count());

        final var actualInvalidEntity = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedId.getValue(), actualInvalidEntity.getId());
        Assertions.assertEquals("Vin Diesel", actualInvalidEntity.getName());
        Assertions.assertEquals(CastMemberType.DIRECTOR, actualInvalidEntity.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualInvalidEntity.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualInvalidEntity.getUpdatedAt());

        // when
        final var anUpdatedCastMember = CastMember.with(aMember);
        final var actualMember = castMemberGateway.update(anUpdatedCastMember.update(expectedName, expectedType));

        // then
        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertTrue(aMember.getUpdatedAt().isBefore(actualMember.getUpdatedAt()));

        final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertTrue(aMember.getUpdatedAt().isBefore(persistedMember.getUpdatedAt()));
    }

    @Test
    public void givenTwoCastMembersAndOnePersisted_whenCallsExistsByIds_shouldReturnPersistedID() {
        // given
        final var aMember = CastMember.newMember("Vin Diesel", CastMemberType.DIRECTOR);

        final var expectedItems = 1;
        final var expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        // when
        final var actualMember = castMemberGateway.existsByIds(List.of(CastMemberID.from("123"), expectedId));

        // then
        Assertions.assertEquals(expectedItems, actualMember.size());
        Assertions.assertEquals(expectedId.getValue(), actualMember.get(0).getValue());
    }

    @Test
    void givenAPrePersistedCastMember_whenTryToDeleteIt_shouldDeleteCastMember() {
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        Assertions.assertEquals(0, castMemberRepository.count());
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        castMemberGateway.deleteById(aMember.getId());

        Assertions.assertEquals(0, castMemberRepository.count());
    }

    @Test
    void givenInvalidCastMemberId_whenCallsDeleteById_shouldReturnOK() {
        //given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        Assertions.assertEquals(1, castMemberRepository.count());
        //when
        castMemberGateway.deleteById(CastMemberID.from("invalid"));
        //then
        Assertions.assertEquals(1, castMemberRepository.count());
    }

    @Test
    public void givenAValidCastMember_whenCallsFindById_shouldReturnIt() {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var aMember = CastMember.newMember(expectedName, expectedType);
        final var expectedId = aMember.getId();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // when
        final var actualMember = castMemberGateway.findById(expectedId).get();

        // then
        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void givenAnInvalidId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // when
        final var actualMember = castMemberGateway.findById(CastMemberID.from("123"));

        // then
        Assertions.assertTrue(actualMember.isEmpty());
    }

    @Test
    public void givenEmptyCastMembers_whenCallsFindAll_shouldReturnEmpty() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = castMemberGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "vin,0,10,1,1,Vin Diesel",
            "taran,0,10,1,1,Quentin Tarantino",
            "jas,0,10,1,1,Jason Momoa",
            "har,0,10,1,1,Kit Harington",
            "MAR,0,10,1,1,Martin Scorsese",
    })
    public void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) throws InterruptedException {
        // given
        mockMembers();

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = castMemberGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Jason Momoa",
            "name,desc,0,10,5,5,Vin Diesel",
            "createdAt,asc,0,10,5,5,Kit Harington",
            "createdAt,desc,0,10,5,5,Martin Scorsese",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) throws InterruptedException {
        // given
        mockMembers();

        final var expectedTerms = "";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = castMemberGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Jason Momoa;Kit Harington",
            "1,2,2,5,Martin Scorsese;Quentin Tarantino",
            "2,2,1,5,Vin Diesel",
    })
    public void givenAValidPagination_whenCallsFindAll_shouldReturnPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedNames
    ) throws InterruptedException {
        // given
        mockMembers();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = castMemberGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName : expectedNames.split(";")) {
            Assertions.assertEquals(expectedName, actualPage.items().get(index).getName());
            index++;
        }
    }

    private void mockMembers() throws InterruptedException {
//        castMemberRepository.saveAllAndFlush(List.of(
//                CastMemberJpaEntity.from(CastMember.newMember("Kit Harington", CastMemberType.ACTOR)),
//                CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)),
//                CastMemberJpaEntity.from(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR)),
//                CastMemberJpaEntity.from(CastMember.newMember("Jason Momoa", CastMemberType.ACTOR)),
//                CastMemberJpaEntity.from(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR))
//        ));
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Kit Harington", CastMemberType.ACTOR)));
        Thread.sleep(1);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)));
        Thread.sleep(1);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR)));
        Thread.sleep(1);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Jason Momoa", CastMemberType.ACTOR)));
        Thread.sleep(1);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR)));
    }
}